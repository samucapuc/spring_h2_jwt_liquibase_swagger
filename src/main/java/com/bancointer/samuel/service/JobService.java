package com.bancointer.samuel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bancointer.samuel.domain.Job;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.JobDTO;
import com.bancointer.samuel.exceptions.InvalidResourceException;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.exceptions.SelfDependenciesException;
import com.bancointer.samuel.repositories.JobRepository;
import com.bancointer.samuel.utils.MessageUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobService {

	private final JobRepository jobRepository;
	private final ModelMapper mapper;
	private final MessageUtils messageUtils;

	@Transactional(readOnly = true)
	public List<JobDTO> findAll(boolean sortByWeight) {
		return !sortByWeight
				? jobRepository.findAll().stream().map(this::converterEntityDTO).collect(Collectors.toList())
				: sort(jobRepository.findAll());
	}

	@Transactional(readOnly = true)
	public Page<JobDTO> findAllWithPagination(boolean sortByWeight, Pageable pageAble) {
		Page<Job> pageJob = jobRepository.findAll(pageAble);
		return !sortByWeight ? pageJob.map(this::converterEntityDTO) : sort(pageJob).map(this::converterEntityDTO);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Page<Job> sort(Page<Job> pageJob) {
		List<Job> listAux = pageJob.getContent().stream().collect(Collectors.toList());
		sort(listAux);
		Page<Job> newPage = new PageImpl(listAux, pageJob.getPageable(), pageJob.getTotalElements());
		return newPage;
	}

	private List<JobDTO> sort(List<Job> listJob) {
		int sumWeightJob1 = 0, sumWeightJob2 = 0;
		listJob.sort((j1, j2) -> compareWeight(j1, j2, sumWeightJob1, sumWeightJob2));
		return listJob.stream().map(this::converterEntityDTO).collect(Collectors.toList());
	}

	private int compareWeight(Job j1, Job j2, int sumWeightJob1, int sumWeightJob2) {
		sumWeightJob1 = CollectionUtils.isEmpty(j1.getTasks()) ? 0
				: j1.getTasks().stream().map(Task::getWeight).mapToInt(Integer::intValue).sum();
		sumWeightJob2 = CollectionUtils.isEmpty(j2.getTasks()) ? 0
				: j2.getTasks().stream().map(Task::getWeight).mapToInt(Integer::intValue).sum();
		return sumWeightJob1 > sumWeightJob2 ? -1 : 0;
	}

	@Transactional(readOnly = true)
	public JobDTO findById(Integer id) {
		JobDTO jobDTO;
		Optional<Job> jobOptional = jobRepository.findById(id);
		if (jobOptional.isPresent()) {
			jobDTO = converterEntityDTO(jobOptional.get());
		} else {
			throw new ObjectNotFoundException(messageUtils.getMessageEnglish("resource.notfound",
					new Object[] { messageUtils.getMessageEnglish("entity.job.name"), "id", id }));
		}
		return jobDTO;
	}

	public JobDTO createJob(JobDTO jobDTO) {
		if (jobDTO.getId() != null) {
			throw new InvalidResourceException(messageUtils.getMessageEnglish("resource.invalid.post",
					new String[] { messageUtils.getMessageEnglish("entity.job.name"), "name", jobDTO.getName() }),RequestMethod.POST);
		}
		return salveJob(jobDTO);
	}

	public JobDTO salveJob(JobDTO jobDTO) {
		validDuplicateJob(jobDTO);
		validSelfDependencies(jobDTO, jobDTO.getId());
		return converterEntityDTO(jobRepository.save(converterDTOEntity(jobDTO)));
	}

	private void validSelfDependencies(JobDTO jobDTO, Integer idJobSalve) {
		if (jobDTO != null && jobDTO.getParentJob() != null && jobDTO.getParentJob().getId().equals(idJobSalve)) {
			throw new SelfDependenciesException(messageUtils.getMessageEnglish("resource.self.dependencies",
					new String[] { messageUtils.getMessageEnglish("entity.job.name"), "name", jobDTO.getName() }));
		} else if (jobDTO != null) {
			validSelfDependencies(jobDTO.getParentJob(), idJobSalve);
		}
	}

	public JobDTO updateJob(JobDTO jobDTO, Integer id) {
		validURIEWithIdPUT(jobDTO, id);
		// Job informed by user
		Job jobEntered = converterDTOEntity(jobDTO);
		// to valid if job exists in data base
		findById(id);
		Job jobSave = new Job();
		BeanUtils.copyProperties(jobEntered, jobSave);
		jobSave.setId(id);
		return salveJob(converterEntityDTO(jobSave));
	}

	private void validURIEWithIdPUT(JobDTO jobDTO, Integer id) {
		if (jobDTO.getId()!=null&&!id.equals(jobDTO.getId())) {
			throw new InvalidResourceException(messageUtils.getMessageEnglish("resource.invalid.put",
					new String[] { messageUtils.getMessageEnglish("entity.job.name"), "name", jobDTO.getName() }),RequestMethod.PUT);
		}
	}

	@Transactional(readOnly = true)
	public List<Job> findByName(String name) {
		return jobRepository.findByNameIgnoreCase(name);
	}

	private void validDuplicateJob(JobDTO jobDTO) {
		List<Job> jobsWithSameName = findByName(jobDTO.getName());
		if (jobsWithSameName.stream().anyMatch(t -> isDuplicate(t, jobDTO))) {
			throw new ObjectDuplicateException(messageUtils.getMessageEnglish("resource.duplicated",
					new String[] { messageUtils.getMessageEnglish("entity.job.name"), "name", jobDTO.getName() }));
		}
	}

	private boolean isDuplicate(Job job, JobDTO jobDTO) {
		if (job.getName().toLowerCase().equals(jobDTO.getName().toLowerCase())
				&& (jobDTO.getId() == null || !job.getId().equals(jobDTO.getId()))) {
			return true;
		}
		return false;
	}

	public void delete(Integer id) {
		jobRepository.delete(converterDTOEntity(findById(id)));
	}

	private JobDTO converterEntityDTO(Job job) {
		JobDTO jobDto = mapper.map(job, JobDTO.class);
		return jobDto;
	}

	private Job converterDTOEntity(JobDTO jobDTO) {
		Job job = mapper.map(jobDTO, Job.class);
		return job;
	}
}
