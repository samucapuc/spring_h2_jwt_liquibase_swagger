package com.bancointer.samuel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancointer.samuel.converter.TaskConverter;
import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.repositories.TaskRepository;
import com.bancointer.samuel.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final TaskConverter taskConverter;
	private final MessageUtils messageUtils;
	

	@Transactional(readOnly = true)
	public List<TaskDTO> findByCreateAt(LocalDate createDate) {
		return taskRepository.findByCreatedAt(createDate).stream().map(t -> taskConverter.converterEntityDTO(t))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<TaskDTO> findByCreateAtPagination(LocalDate createDate, Pageable page) {
		return taskRepository.findByCreatedAt(createDate, page).map(t -> taskConverter.converterEntityDTO(t));
	}
	
	@Transactional(readOnly = true)
	public TaskDTO findById(Integer id) {
		TaskDTO taskDTO;
		Optional<Task> taskOptional = taskRepository.findById(id);
		if(taskOptional.isPresent()){
			taskDTO=taskConverter.converterEntityDTO(taskOptional.get());
		}else {
			throw new ObjectNotFoundException(messageUtils.getMessageEnglish("resource.notfound",
					new Object[] { messageUtils.getMessageEnglish("entity.task.name"), "id", id }));
		}
		return taskDTO;
	}
	
	public TaskDTO salveTask(TaskDTO taskDTO) {
		validDuplicateTask(taskDTO);
		return taskConverter.converterEntityDTO(taskRepository.save(taskConverter.converterDTOEntity(taskDTO)));
	}
	public TaskDTO updateTask(TaskDTO taskDTO,Integer id) {
		//Task task = taskConverter.converterDTOEntity(taskDTO);
		taskDTO.setId(id);
		return salveTask(taskDTO);
	}
	
	@Transactional(readOnly = true)
	public List<Task> findByName(String name){
		return taskRepository.findByName(name);
	}
	
	private void validDuplicateTask(TaskDTO taskDTO) {
		List<Task> tasksWithSameName = findByName(taskDTO.getName());
		if (tasksWithSameName.stream().anyMatch(t -> isDuplicate(t, taskDTO))) {
			throw new ObjectDuplicateException(messageUtils.getMessageEnglish("resource.duplicated",
					new String[] { messageUtils.getMessageEnglish("entity.task.name"), "name", taskDTO.getName() }));
		}
	}

	private boolean isDuplicate(Task task, TaskDTO taskDTO) {
		if (task.getName().equals(taskDTO.getName())
				&& (taskDTO.getId() == null || !task.getId().equals(taskDTO.getId()))) {
			return true;
		}
		return false;
	}

	public void delete(Integer id) {
		taskRepository.delete(taskConverter.converterDTOEntity(findById(id)));
	}
}
