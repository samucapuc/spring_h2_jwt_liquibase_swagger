package com.bancointer.samuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bancointer.samuel.domain.Job;
import com.bancointer.samuel.dto.JobDTO;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.repositories.JobRepository;
import com.bancointer.samuel.service.JobService;
import com.bancointer.samuel.utils.MessageUtils;

public class JobResourceTest extends SamuelApplicationTests {

	@InjectMocks
	private JobService jobService;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private MessageUtils message;
	
	@Mock
	private ModelMapper mapper;

	@Test
	public void mustListJobsSortByWeight() {
		when(jobRepository.findAll())
				.thenReturn(Arrays.asList(new Job(1, "FIRST JOB", true, null, null,0)));
		List<JobDTO> job = jobService.findAll(true);
		assertNotNull(job);
		assertEquals(job.size(), 1);
	}
	@Test
	public void mustListJobsWithOutSortByWeight() {
		when(jobRepository.findAll())
		.thenReturn(Arrays.asList(new Job(1, "FIRST JOB", true, null, null,0)));
		List<JobDTO> job = jobService.findAll(false);
		assertNotNull(job);
		assertEquals(job.size(), 1);
	}

	@Test
	public void mustListJobsPaginated() {
		Pageable firstPageWithTwoElements = PageRequest.of(0, 2, Sort.by("name").ascending());
		Page<Job> pageFound = new PageImpl<>(
				Arrays.asList(new Job(1, "FIRST JOB", true, null, null,0),
						new Job(2, "SECOND JOB", true, null, null,0)),
				firstPageWithTwoElements, 2);
		when(jobRepository.findAll(Mockito.any(Pageable.class)))
				.thenReturn(pageFound);
		Page<JobDTO> jobPaged = jobService.findAllWithPagination(true, firstPageWithTwoElements);
		assertEquals(pageFound.getNumberOfElements(), jobPaged.getNumberOfElements());
	}
	
	@Test
	public void mustFindOneTask() {
		Optional<Job> jobOptional = Optional.of(new Job(1, "FIRST JOB", true, null, null,0));
		when(jobRepository.findById(Mockito.any(Integer.class))).thenReturn(jobOptional);
		JobDTO job = jobService.findById(1);
		assertNotNull(job);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void mustValidObjectNotFoundSearchTask() {
		Optional<Job> jobOptional = Optional.empty();
		when(jobRepository.findById(Mockito.any(Integer.class))).thenReturn(jobOptional)
				.thenThrow(ObjectNotFoundException.class);
		when(message.getMessageEnglish(Mockito.anyString(), Mockito.any(Object[].class)))
				.thenReturn("The requested resource Job could not be found with id 6");
		jobService.findById(6);
	}
}
