package com.bancointer.samuel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.repositories.TaskRepository;
import com.bancointer.samuel.service.TaskService;

public class TaskRestControllerTest extends SamuelApplicationTests {
	
	@InjectMocks
	private TaskService taskService;
	
	@Mock
	private TaskRepository taskRepository;
	
	@Test
	public void mustListTasks() {
		when(taskRepository.findByCreatedAt(Mockito.any(LocalDate.class))).thenReturn(Arrays.asList(new Task(1L,"First task",5,true,LocalDate.now(),0),new Task(2L,"Second task",7,false,LocalDate.now(),1)));
		List<TaskDTO> task = taskService.findByCreateAt(LocalDate.now());
		assertNotNull(task);
		assertNotEquals(task.size(), 1);
		assertEquals(task.size(), 2);
	}

}
