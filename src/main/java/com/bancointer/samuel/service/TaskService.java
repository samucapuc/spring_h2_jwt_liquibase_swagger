package com.bancointer.samuel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.exceptions.InvalidResourceException;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.repositories.TaskRepository;
import com.bancointer.samuel.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final MessageUtils messageUtils;
	private final ModelMapper mapper;

	@Transactional(readOnly = true)
	public List<TaskDTO> findByCreateAt(LocalDate createDate) {
		return taskRepository.findByCreatedAt(createDate).stream().map(this::converterEntityDTO)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<TaskDTO> findByCreateAtPagination(LocalDate createDate, Pageable page) {
		return taskRepository.findByCreatedAt(createDate, page).map(this::converterEntityDTO);
	}

	@Transactional(readOnly = true)
	public TaskDTO findById(Integer id) {
		TaskDTO taskDTO;
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isPresent()) {
			taskDTO = converterEntityDTO(taskOptional.get());
		} else {
			throw new ObjectNotFoundException(messageUtils.getMessageEnglish("resource.notfound",
					new Object[] { messageUtils.getMessageEnglish("entity.task.name"), "id", id }));
		}
		return taskDTO;
	}

	public TaskDTO createTask(TaskDTO taskDTO) {
		if (taskDTO.getId() != null) {
			throw new InvalidResourceException(messageUtils.getMessageEnglish("resource.invalid.post",
					new String[] { messageUtils.getMessageEnglish("entity.task.name"), "name", taskDTO.getName() }),HttpMethod.POST);
		}
		return salveTask(taskDTO);
	}

	public TaskDTO salveTask(TaskDTO taskDTO) {
		validDuplicateTask(taskDTO);
		return converterEntityDTO(taskRepository.save(converterDTOEntity(taskDTO)));
	}

	public TaskDTO updateTask(TaskDTO taskDTO, Integer id) {
		//Task informed by user
		Task taskEntered = converterDTOEntity(taskDTO);
		//to valid if task exists in data base
		findById(id);
		Task taskSave = new Task();
		BeanUtils.copyProperties(taskEntered, taskSave);
		taskSave.setId(id);
		return salveTask(converterEntityDTO(taskSave));
	}

	@Transactional(readOnly = true)
	public List<Task> findByName(String name) {
		return taskRepository.findByNameIgnoreCase(name);
	}

	
	private void validDuplicateTask(TaskDTO taskDTO) {
		List<Task> tasksWithSameName = findByName(taskDTO.getName());
		if (tasksWithSameName.stream().anyMatch(t -> isDuplicate(t, taskDTO))) {
			throw new ObjectDuplicateException(messageUtils.getMessageEnglish("resource.duplicated",
					new String[] { messageUtils.getMessageEnglish("entity.task.name"), "name", taskDTO.getName() }));
		}
	}

	private boolean isDuplicate(Task task, TaskDTO taskDTO) {
		if (task.getName().toLowerCase().equals(taskDTO.getName().toLowerCase())
				&& (taskDTO.getId() == null || !task.getId().equals(taskDTO.getId()))) {
			return true;
		}
		return false;
	}

	public void delete(Integer id) {
		taskRepository.delete(converterDTOEntity(findById(id)));
	}

	private TaskDTO converterEntityDTO(Task task) {
		TaskDTO taskDto = mapper.map(task, TaskDTO.class);
		return taskDto;
	}

	private Task converterDTOEntity(TaskDTO taskDTO) {
		Task task = mapper.map(taskDTO, Task.class);
		return task;
	}
}
