package com.bancointer.samuel.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.bancointer.samuel.exceptions.ErrorResponse;
import com.bancointer.samuel.exceptions.StandardError;
import com.bancointer.samuel.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@ControllerAdvice()
@RequiredArgsConstructor
public class ConstraintViolationExceptionHandler {

	private final MessageUtils messageUtils;

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("param.required.title"),
				messageUtils.getMessageEnglish("param.required", new Object[] { ex.getParameterName() }),
				((ServletWebRequest) request).getRequest().getRequestURL().toString(), request.getDescription(false));
		return ResponseEntity.badRequest().body(err);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		List<StandardError> listError = new ArrayList<StandardError>();
		for (String error : ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.toList())) {
			listError.add(new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
					messageUtils.getMessageEnglish("error.validation.title"),
					messageUtils.getMessageEnglish("error.validation", new Object[] { error }),
					((ServletWebRequest) request).getRequest().getRequestURL().toString(),
					request.getDescription(false)));
		}
		return new ResponseEntity<>(new ErrorResponse<>(listError), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<?> handleConstraintViolationException(IllegalArgumentException e,
			HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("error.conversion"), e.getMessage(), e.getMessage(),
				request.getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
