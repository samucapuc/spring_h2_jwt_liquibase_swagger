package com.bancointer.samuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bancointer.samuel.converter.TaskConverter;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.repositories.TaskRepository;
import com.bancointer.samuel.service.TaskService;

public class TaskResourceTest extends SamuelApplicationTests {

	@InjectMocks
	private TaskService taskService;

	@Mock
	private TaskConverter taskConverter;

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private MessageSource message;

	@Test
	public void mustListTasks() {
		when(taskRepository.findByCreatedAt(Mockito.any(LocalDate.class)))
				.thenReturn(Arrays.asList(new Task(1L, "First task", 5, true, LocalDate.now(), 0, null),
						new Task(2L, "Second task", 7, false, LocalDate.now(), 1, null)));
		List<TaskDTO> task = taskService.findByCreateAt(LocalDate.now());
		assertNotNull(task);
		assertNotEquals(task.size(), 1);
		assertEquals(task.size(), 2);
	}
	
	@Test
	public void mustListTasksPaginated() {
		Pageable firstPageWithTwoElements = PageRequest.of(0, 2,Sort.by("name").ascending());
		Page<Task> pageFound = new PageImpl<>(Arrays.asList(new Task(1L, "First task", 5, true, LocalDate.now(), 0, null),
				new Task(2L, "Second task", 7, false, LocalDate.now(), 1, null)), firstPageWithTwoElements, 2);
		when(taskRepository.findByCreatedAt(Mockito.any(LocalDate.class),Mockito.any(Pageable.class)))
				.thenReturn(pageFound);
		Page<TaskDTO> taskPaged = taskService.findByCreateAtPagination(LocalDate.now(),firstPageWithTwoElements);
		assertEquals(pageFound.getNumberOfElements(), taskPaged.getNumberOfElements());
	}

	@Test
	public void mustCreateTask() {
		TaskDTO taskDTO = new TaskDTO(2L, "Second task", 6, true, LocalDate.now(), 0);
		Task task = new Task(2L, "Second task", 6, true, LocalDate.now(), 0,null);
		when(taskConverter.converterEntityDTO(Mockito.any(Task.class))).thenReturn(taskDTO);
		when(taskConverter.converterDTOEntity(Mockito.any(TaskDTO.class))).thenReturn(task);
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
		taskDTO = taskService.salveTask(taskDTO);
		assertEquals(task.getId(),taskDTO.getId());
	}

	@Test(expected = ObjectDuplicateException.class)
	public void mustValidDuplicatedTask() {
		TaskDTO taskDTO = new TaskDTO(1L, "First task", 5, true, LocalDate.now(), 0);
		when(taskRepository.findByName(Mockito.any(String.class)))
				.thenReturn(Arrays.asList(new Task(2L, "First task", 8, false, LocalDate.now(), 0, null)))
				.thenThrow(ObjectDuplicateException.class);
		when(message.getMessage(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Locale.class)))
				.thenReturn("There is already a resource Task with name " + taskDTO.getName()
						+ " saved with the same characteristics");
		taskService.salveTask(taskDTO);
	}

	@Test
	public void mustUpdateTask() {
		Task task = new Task(1L, "First task", 5, true, LocalDate.now(), 0,null);
		TaskDTO taskDTO1 = new TaskDTO(1L, "First task", 5, true, LocalDate.now(), 0);
		TaskDTO taskDTO2 = new TaskDTO(1L, "Third task", 5, true, LocalDate.now(), 0);
		when(taskConverter.converterEntityDTO(Mockito.any(Task.class))).thenReturn(taskDTO2);
		when(taskConverter.converterDTOEntity(Mockito.any(TaskDTO.class))).thenReturn(task);
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
		taskDTO2 = taskService.salveTask(taskDTO1);
		assertNotEquals(taskDTO2.getName(), taskDTO1.getName());
		
	}

}
