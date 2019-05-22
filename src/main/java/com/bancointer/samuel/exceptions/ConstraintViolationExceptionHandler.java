package com.bancointer.samuel.exceptions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bancointer.samuel.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@ControllerAdvice()
@RequiredArgsConstructor
public class ConstraintViolationExceptionHandler {

	private final MessageUtils messageUtils;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("param.required.title"),
				messageUtils.getMessageEnglish("param.required", new Object[] { ex.getParameterName().toString() }),
				((ServletWebRequest) request).getRequest().getRequestURL().toString(), request.getDescription(false));
		return ResponseEntity.badRequest().body(err);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex,
			HttpServletRequest request) {
		try {
			List<String> messages = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
					.collect(Collectors.toList());
			return new ResponseEntity<>(new ErrorResponse<>(messages), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse<>(Arrays.asList(ex.getMessage())),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<?> handleConstraintViolationException(IllegalArgumentException e,
			HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),messageUtils.getMessageEnglish("error.conversion"), e.getMessage(), e.getMessage(),((ServletWebRequest)request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
