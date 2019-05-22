package com.bancointer.samuel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	
	private final TaskRepository taskRepository;
	private ModelMapper mapper;

	public List<TaskDTO> findByCreateAt(LocalDate createDate){
		return taskRepository.findByCreatedAt(createDate).stream().map(this::converterEntityDTO).collect(Collectors.toList());
	}
	
	public List<TaskDTO> findByCreateAtPagination(LocalDate createDate,Pageable page){
		return taskRepository.findByCreatedAt(createDate,page).stream().map(this::converterEntityDTO).collect(Collectors.toList());
	}
	
	private TaskDTO converterEntityDTO(Task task) {
		TaskDTO taskDto = mapper.map(task, TaskDTO.class);
	    return taskDto;
	}

}
