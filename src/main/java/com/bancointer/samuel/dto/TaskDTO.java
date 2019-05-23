package com.bancointer.samuel.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private Integer weight;
	private Boolean completed;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;
	
	private Integer version;
	
}
