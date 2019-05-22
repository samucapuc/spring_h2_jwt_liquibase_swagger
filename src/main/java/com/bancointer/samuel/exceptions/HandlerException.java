package com.bancointer.samuel.exceptions;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.bancointer.samuel.utils.MessageUtils;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class HandlerException {
	
	private final MessageUtils messageUtils;
	
	
	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody	
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),messageUtils.getMessageEnglish("resource.notfound.title"), e.getMessage(), messageUtils.getMessageEnglish("resource.notfound.details"),((ServletWebRequest)request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(ObjectDuplicateException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody	
	public ResponseEntity<StandardError> objectDuplicated(ObjectDuplicateException e, WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(),messageUtils.getMessageEnglish("resource.duplicated.title"), e.getMessage(), messageUtils.getMessageEnglish("resource.duplicated.details"),((ServletWebRequest)request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}
	@ExceptionHandler(ConversionFailedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody	
	public ResponseEntity<StandardError> conversionError(ConversionFailedException e, WebRequest request) {
		String message = e.getCause()!=null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage();
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),messageUtils.getMessageEnglish("error.conversion"), message, e.getMessage(),((ServletWebRequest)request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
}
