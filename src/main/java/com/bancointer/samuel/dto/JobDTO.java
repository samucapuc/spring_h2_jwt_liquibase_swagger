package com.bancointer.samuel.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import com.bancointer.samuel.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	private Boolean active;

	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(value = { "tasks" })
	private JobDTO parentJob;

	private List<TaskDTO> tasks;
	
	@NotNull
	@Digits(integer = 999999999,fraction = 0)
	@Min(0)
	private Integer version;
	
	public void setName(String name){
		//Cross-site Scripting prevent
		this.name=StringUtils.isNotBlank(name) ? Utils.removeSpecialCaracters(name) : name;
	}
}
