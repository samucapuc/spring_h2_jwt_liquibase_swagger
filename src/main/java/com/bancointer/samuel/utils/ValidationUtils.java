package com.bancointer.samuel.utils;

import org.springframework.validation.Errors;

public class ValidationUtils extends org.springframework.validation.ValidationUtils {

	public static void rejectIfEmptyOrWhitespace(Errors errors, String[] fields, String errorCode) {
		for (String field : fields) {
			rejectIfEmptyOrWhitespace(errors, field, errorCode);
		}
	}
}
