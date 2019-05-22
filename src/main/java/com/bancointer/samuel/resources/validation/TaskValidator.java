package com.bancointer.samuel.resources.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bancointer.samuel.domain.Task;
import com.bancointer.samuel.dto.TaskDTO;
import com.bancointer.samuel.utils.ValidationUtils;

@Component("taskValidator")
public class TaskValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return TaskDTO.class.isAssignableFrom(clazz) || Task.class.isAssignableFrom(clazz) ;
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, new String[]{"id","name","weight"}, "field.required");
	}

}
