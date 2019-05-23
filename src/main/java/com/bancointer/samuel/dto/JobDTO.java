package com.bancointer.samuel.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	private Boolean active;

	private JobDTO parentJob;

	private List<TaskDTO> tasks;
	
	@NotNull
	@Digits(integer = 999999999,fraction = 0)
	@Min(0)
	private Integer version;

}
