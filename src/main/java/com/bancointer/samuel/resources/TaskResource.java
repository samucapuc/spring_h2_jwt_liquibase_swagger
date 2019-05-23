package com.bancointer.samuel.resources;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tasks")
@Validated
public class TaskResource {

	private final TaskService taskService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TaskDTO>> findByCreateAt(@DateTimeFormat(iso = ISO.DATE) @RequestParam @NotNull(message = "{param.required}")  LocalDate createdAt) {
		return ResponseEntity.ok().body(taskService.findByCreateAt(createdAt));
	}
	
	@GetMapping(value = "/pages",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<TaskDTO>> findByCreateAtPagination(@DateTimeFormat(iso = ISO.DATE) @RequestParam  @NotNull(message = "{param.required}") LocalDate createdAt , Pageable page) {
		return ResponseEntity.ok(taskService.findByCreateAtPagination(createdAt,page));
	}
	
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TaskDTO> findById(@PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		return ResponseEntity.ok().body(taskService.findById(id));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDTO taskDTO) {
		taskDTO = taskService.createTask(taskDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(taskDTO.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateTask( @Valid @RequestBody TaskDTO taskDTO, @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		taskService.updateTask(taskDTO, id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask( @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		taskService.delete(id);
		return ResponseEntity.ok().build();
	}
}
