package com.bancointer.samuel.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancointer.samuel.domain.Job;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.JobDTO;
import com.bancointer.samuel.repositories.JobRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JobService {

	private final JobRepository jobRepository;
	private final ModelMapper mapper;

	@Transactional(readOnly = true)
	public List<JobDTO> findAll(Boolean sortByWeight) {
		return !sortByWeight ? jobRepository.findAll().stream().map(this::converterEntityDTO).collect(Collectors.toList()) : sort(jobRepository.findAll());
	}
	
	private List<JobDTO> sort(List<Job> listJob) {
		int sumWeightJob1 = 0, sumWeightJob2 = 0;
		listJob.sort((j1, j2)-> compareWeight(j1, j2, sumWeightJob1, sumWeightJob2));
		return listJob.stream().map(this::converterEntityDTO).collect(Collectors.toList());
	}
	
	private int compareWeight(Job j1, Job j2,int sumWeightJob1,int sumWeightJob2) {
		sumWeightJob1 = j1.getTasks().isEmpty() ? 0
				: j1.getTasks().stream().map(Task::getWeight).mapToInt(Integer::intValue).sum();
		sumWeightJob2 = j2.getTasks().isEmpty() ? 0
				: j2.getTasks().stream().map(Task::getWeight).mapToInt(Integer::intValue).sum();
		return sumWeightJob1 > sumWeightJob2 ? -1 : 0;
	}
	

	private JobDTO converterEntityDTO(Job job) {
		JobDTO jobDto = mapper.map(job, JobDTO.class);
		return jobDto;
	}
}
