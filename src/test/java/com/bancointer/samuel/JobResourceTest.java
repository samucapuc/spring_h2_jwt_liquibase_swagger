package com.bancointer.samuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import com.bancointer.samuel.domain.Job;
import com.bancointer.samuel.dto.JobDTO;
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

}
