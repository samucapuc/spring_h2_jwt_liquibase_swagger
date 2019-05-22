package com.bancointer.samuel.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskConverter {
	
	private final ModelMapper mapper;

	public TaskDTO converterEntityDTO(Task task) {
		TaskDTO taskDto = mapper.map(task, TaskDTO.class);
	    return taskDto;
	}
	public Task converterDTOEntity(TaskDTO taskDTO) {
		Task task = mapper.map(taskDTO, Task.class);
		return task;
	}
}
