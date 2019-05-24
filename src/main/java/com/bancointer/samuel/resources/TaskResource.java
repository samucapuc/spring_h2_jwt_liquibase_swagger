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
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tasks")
@Validated
@Api(value = "/tasks", description = "Services for maintenance of tasks")
public class TaskResource {

	private final TaskService taskService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITED')")
	@ApiOperation(value="Find all tasks by createdAt")
	@ApiResponses(value = {
			 @ApiResponse(code = 400, message = "Validation Error")
			,@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized")})
	public ResponseEntity<List<TaskDTO>> findByCreateAt(@DateTimeFormat(iso = ISO.DATE) @RequestParam @NotNull(message = "{param.required}")  LocalDate createdAt) {
		return ResponseEntity.ok().body(taskService.findByCreateAt(createdAt));
	}
	
	@GetMapping(value = "/pages",produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITED')")
	@ApiOperation(value="Same result of /tasks, but paged",notes = "You must to pass &page=<PAGE>&size=<NUMBER_PAGE>&sort=[FIELD_SORT],[ORDER]")
	@ApiResponses(value = {
			 @ApiResponse(code = 400, message = "Validation Error")
			,@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized") })	
	public ResponseEntity<Page<TaskDTO>> findByCreateAtPagination(@DateTimeFormat(iso = ISO.DATE) @RequestParam  @NotNull(message = "{param.required}") LocalDate createdAt , Pageable page) {
		return ResponseEntity.ok(taskService.findByCreateAtPagination(createdAt,page));
	}
	
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INVITED')")
	@ApiOperation(value="Find a one task by id")
	@ApiResponses(value = {
			 @ApiResponse(code = 400, message = "Validation Error")
			,@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 404, message = "Resource not found")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized") })		
	public ResponseEntity<TaskDTO> findById(@PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		return ResponseEntity.ok().body(taskService.findById(id));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Create a task")
	@ApiResponses(value = {
			 @ApiResponse(code = 400, message = "Validation Error")
			,@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 400, message = "Invalid Resource")
			,@ApiResponse(code = 409, message = "Duplicate resource")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized") })		
	public ResponseEntity<Void> createTask(@Valid @RequestBody TaskDTO taskDTO) {
		taskDTO = taskService.createTask(taskDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(taskDTO.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Update a task")
	@ApiResponses(value = {
			 @ApiResponse(code = 400, message = "Validation Error")
			,@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 400, message = "Concurrency error")
			,@ApiResponse(code = 404, message = "Resource not found")
			,@ApiResponse(code = 400, message = "Invalid Resource")
			,@ApiResponse(code = 409, message = "Duplicate resource")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized") })	
	public ResponseEntity<Void> updateTask( @Valid @RequestBody TaskDTO taskDTO, @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		taskService.updateTask(taskDTO, id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Delete a task")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Conversion Error")
			,@ApiResponse(code = 404, message = "Resource not found")
			,@ApiResponse(code = 401, message = "User not authenticated")
			,@ApiResponse(code = 403, message = "User not authorized") })	
	public ResponseEntity<Void> deleteTask( @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		taskService.delete(id);
		//O verbo HTTP DELETE deve retornar o código 200 se inclui uma entidade no response ou 204 se não inclui
		return ResponseEntity.noContent().build();
	}
}
