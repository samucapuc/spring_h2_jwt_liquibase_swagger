package com.bancointer.samuel.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.bancointer.samuel.dto.JobDTO;
import com.bancointer.samuel.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/jobs")
@Validated
public class JobResource {
	
	private final JobService jobService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<JobDTO>> findAll(@RequestParam @NotNull(message = "{param.required}")  Boolean sortByWeight) {
		return ResponseEntity.ok().body(jobService.findAll(sortByWeight));
	}
	@GetMapping(value = "/pages",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<JobDTO>> findByCreateAtPagination(@RequestParam @NotNull(message = "{param.required}")  Boolean sortByWeight , Pageable pageAble) {
		return ResponseEntity.ok(jobService.findAllWithPagination(sortByWeight, pageAble));
	}
	
	@GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JobDTO> findById(@PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		return ResponseEntity.ok().body(jobService.findById(id));
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createTask(@Valid @RequestBody JobDTO jobDTO) {
		jobDTO = jobService.createJob(jobDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(jobDTO.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateTask( @Valid @RequestBody JobDTO jobDTO, @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		jobService.updateJob(jobDTO, id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask( @PathVariable() @NotNull(message = "{param.required}") @Min(1)  Integer id) {
		jobService.delete(id);
		//O verbo HTTP DELETE deve retornar o código 200 se inclui uma entidade no response ou 204 se não inclui
		return ResponseEntity.noContent().build();
	}
	
}
