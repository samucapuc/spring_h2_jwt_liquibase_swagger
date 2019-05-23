package com.bancointer.samuel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.bancointer.samuel.domain.Job;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.JobDTO;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
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
		return !sortByWeight ? pageJob.map(this::converterEntityDTO) : sort(pageJob);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Page<JobDTO> sort(Page<Job> pageJob) {
		List<Job> listAux = pageJob.getContent().stream().collect(Collectors.toList());
		sort(listAux);
		Page<JobDTO> newPage = new PageImpl(listAux, pageJob.getPageable(), pageJob.getTotalElements());
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

	private JobDTO converterEntityDTO(Job job) {
		JobDTO jobDto = mapper.map(job, JobDTO.class);
		return jobDto;
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

}
