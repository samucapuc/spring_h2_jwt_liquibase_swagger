package com.bancointer.samuel.dto;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
	@NotNull
	@NotEmpty
	private String name;
	@Min(0)
	@NotNull
	private Integer weight;
	private Boolean completed;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;
	
	@NotNull
	@Digits(integer = 999999999,fraction = 0)
	@Min(0)
	private Integer version;
	
}
