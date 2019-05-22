package com.bancointer.samuel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bancointer.samuel.converter.TaskConverter;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.repositories.TaskRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskService extends MainService{

	private final TaskRepository taskRepository;
	private final TaskConverter taskConverter;

	public List<TaskDTO> findByCreateAt(LocalDate createDate) {
		return taskRepository.findByCreatedAt(createDate).stream().map(t -> taskConverter.converterEntityDTO(t))
				.collect(Collectors.toList());
	}

	public Page<TaskDTO> findByCreateAtPagination(LocalDate createDate, Pageable page) {
		return taskRepository.findByCreatedAt(createDate, page).map(t -> taskConverter.converterEntityDTO(t));
	}

	public TaskDTO salveTask(TaskDTO taskDTO) {
		validDuplicateTask(taskDTO);
		return taskConverter.converterEntityDTO(taskRepository.save(taskConverter.converterDTOEntity(taskDTO)));
	}

	private void validDuplicateTask(TaskDTO taskDTO) {
		List<Task> tasksWithSameName = taskRepository.findByName(taskDTO.getName());
		if (tasksWithSameName.stream().anyMatch(t -> isDuplicate(t, taskDTO))) {
			throw new ObjectDuplicateException(getMessageEnglish("object.duplicated",
					new String[] { getMessageEnglish("entity.task.name"), "name", taskDTO.getName() }));
		}
	}

	private boolean isDuplicate(Task task, TaskDTO taskDTO) {
		if (task.getName().equals(taskDTO.getName())
				&& (taskDTO.getId() == null || !task.getId().equals(taskDTO.getId()))) {
			return true;
		}
		return false;
	}

}
