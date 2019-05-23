package com.bancointer.samuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.repositories.TaskRepository;
import com.bancointer.samuel.service.TaskService;
import com.bancointer.samuel.utils.MessageUtils;

public class TaskResourceTest extends SamuelApplicationTests {

	@InjectMocks
	private TaskService taskService;

	@Mock
	private TaskRepository taskRepository;

	@Mock
	private MessageUtils message;

	@Test
	public void mustListTasks() {
		when(taskRepository.findByCreatedAt(Mockito.any(LocalDate.class)))
				.thenReturn(Arrays.asList(new Task(1, "First task", 5, true, LocalDate.now(), 0, null),
						new Task(2, "Second task", 7, false, LocalDate.now(), 1, null)));
		List<TaskDTO> task = taskService.findByCreateAt(LocalDate.now());
		assertNotNull(task);
		assertNotEquals(task.size(), 1);
		assertEquals(task.size(), 2);
	}

	@Test
	public void mustListTasksPaginated() {
		Pageable firstPageWithTwoElements = PageRequest.of(0, 2, Sort.by("name").ascending());
		Page<Task> pageFound = new PageImpl<>(
				Arrays.asList(new Task(1, "First task", 5, true, LocalDate.now(), 0, null),
						new Task(2, "Second task", 7, false, LocalDate.now(), 1, null)),
				firstPageWithTwoElements, 2);
		when(taskRepository.findByCreatedAt(Mockito.any(LocalDate.class), Mockito.any(Pageable.class)))
				.thenReturn(pageFound);
		Page<TaskDTO> taskPaged = taskService.findByCreateAtPagination(LocalDate.now(), firstPageWithTwoElements);
		assertEquals(pageFound.getNumberOfElements(), taskPaged.getNumberOfElements());
	}

	@Test
	public void mustFindOneTask() {
		Optional<Task> taskOptional = Optional.of(new Task(1, "First task", 5, true, LocalDate.now(), 0, null));
		when(taskRepository.findById(Mockito.any(Integer.class))).thenReturn(taskOptional);
		TaskDTO task = taskService.findById(1);
		assertNotNull(task);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void mustValidObjectNotFoundSearchTask() {
		Optional<Task> taskOptional = Optional.empty();
		when(taskRepository.findById(Mockito.any(Integer.class))).thenReturn(taskOptional)
				.thenThrow(ObjectNotFoundException.class);
		when(message.getMessageEnglish(Mockito.anyString(), Mockito.any(Object[].class)))
				.thenReturn("The requested resource Task could not be found with id 2");
		taskService.findById(2);
	}

	@Test
	public void mustCreateTask() {
		TaskDTO taskDTO = new TaskDTO(2, "Second task", 6, true, LocalDate.now(), 0);
		Task task = new Task(2, "Second task", 6, true, LocalDate.now(), 0, null);
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
		taskDTO = taskService.salveTask(taskDTO);
		assertEquals(task.getId(), taskDTO.getId());
	}

	@Test(expected = ObjectDuplicateException.class)
	public void mustValidDuplicatedTask() {
		TaskDTO taskDTO = new TaskDTO(1, "First task", 5, true, LocalDate.now(), 0);
		when(taskRepository.findByName(Mockito.any(String.class)))
				.thenReturn(Arrays.asList(new Task(2, "First task", 8, false, LocalDate.now(), 0, null)))
				.thenThrow(ObjectDuplicateException.class);
		when(message.getMessageEnglish(Mockito.anyString(), Mockito.any(Object[].class)))
				.thenReturn("There is already a resource Task with name " + taskDTO.getName()
						+ " saved with the same characteristics");
		taskService.salveTask(taskDTO);
	}

	@Test
	public void mustUpdateTask() {
		Task task = new Task(1, "First task", 5, true, LocalDate.now(), 0, null);
		TaskDTO taskDTO1 = new TaskDTO(1, "First task", 5, true, LocalDate.now(), 0);
		TaskDTO taskDTO2 = new TaskDTO(1, "Third task", 5, true, LocalDate.now(), 0);
		when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
		taskDTO2 = taskService.salveTask(taskDTO1);
		assertNotEquals(taskDTO2.getName(), taskDTO1.getName());
	}

	@Test
	public void mustDeleteTask() {
		Optional<Task> taskOptional = Optional.of(new Task(1, "First task", 5, true, LocalDate.now(), 0, null));
		when(taskRepository.findById(Mockito.any(Integer.class))).thenReturn(taskOptional);
		taskService.delete(1);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void mustValidObjectNotFoundDeleteTask() {
		Optional<Task> taskOptional = Optional.empty();
		when(taskRepository.findById(Mockito.any(Integer.class))).thenReturn(taskOptional)
				.thenThrow(ObjectNotFoundException.class);
		when(message.getMessageEnglish(Mockito.anyString(), Mockito.any(Object[].class)))
				.thenReturn("The requested resource Task could not be found with id 2");
		taskService.delete(2);
	}
}
