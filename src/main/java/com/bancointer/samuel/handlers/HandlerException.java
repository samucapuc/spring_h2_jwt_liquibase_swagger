package com.bancointer.samuel.handlers;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.TransientPropertyValueException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.bancointer.samuel.exceptions.InvalidResourceException;
import com.bancointer.samuel.exceptions.ObjectDuplicateException;
import com.bancointer.samuel.exceptions.ObjectNotFoundException;
import com.bancointer.samuel.exceptions.SelfDependenciesException;
import com.bancointer.samuel.exceptions.StandardError;
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
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				messageUtils.getMessageEnglish("resource.notfound.title"), e.getMessage(),
				messageUtils.getMessageEnglish("resource.notfound.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseEntity<StandardError> EntityNotFoundException(EntityNotFoundException e, WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(),
				messageUtils.getMessageEnglish("resource.notfound.title"),
				messageUtils.getMessageEnglish("resource.notfound.handler",
						new String[] { (StringUtils.isNoneBlank(e.getMessage())
								&& e.getMessage().contains("com.bancointer.samuel.domain"))
										? e.getMessage().substring(e.getMessage().indexOf("domain.")).replaceAll(
												"domain.", StringUtils.EMPTY)
										: StringUtils.EMPTY }),
				messageUtils.getMessageEnglish("resource.notfound.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}

	@ExceptionHandler(InvalidResourceException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> objectNotFound(InvalidResourceException e, WebRequest request) {
		StandardError err;
		switch (e.getHttpMethod()) {
		case POST:
			err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
					messageUtils.getMessageEnglish("resource.invalid.post.title"), e.getMessage(),
					messageUtils.getMessageEnglish("resource.invalid.post.details"),
					((ServletWebRequest) request).getRequest().getRequestURL().toString());
			break;

		default:
			err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
					messageUtils.getMessageEnglish("resource.invalid.put.title"), e.getMessage(),
					messageUtils.getMessageEnglish("resource.invalid.put.details"),
					((ServletWebRequest) request).getRequest().getRequestURL().toString());
			break;
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(ObjectDuplicateException.class)
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ResponseBody
	public ResponseEntity<StandardError> objectDuplicated(ObjectDuplicateException e, WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.CONFLICT.value(),
				messageUtils.getMessageEnglish("resource.duplicated.title"), e.getMessage(),
				messageUtils.getMessageEnglish("resource.duplicated.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
	}

	@ExceptionHandler(ConversionFailedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> conversionError(ConversionFailedException e, WebRequest request) {
		String message = e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage();
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("error.conversion"), message, e.getMessage(),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(ObjectOptimisticLockingFailureException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> ObjectOptimisticLockingError(ObjectOptimisticLockingFailureException e,
			WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("error.concurrency.title"),
				messageUtils.getMessageEnglish("error.concurrency"),
				messageUtils.getMessageEnglish("error.concurrency.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(SelfDependenciesException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> SelfDependenciesException(SelfDependenciesException e, WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("resource.self.dependencies.title"),
				messageUtils.getMessageEnglish("resource.self.dependencies"),
				messageUtils.getMessageEnglish("resource.self.dependencies.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> DataIntegrityViolationException(DataIntegrityViolationException e,
			WebRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(),
				messageUtils.getMessageEnglish("data.integrity.violation.exception.title"),
				messageUtils.getMessageEnglish("data.integrity.violation.exception"),
				messageUtils.getMessageEnglish("data.integrity.violation.exception.details"),
				((ServletWebRequest) request).getRequest().getRequestURL().toString());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<StandardError> InvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e,
			WebRequest request) {
		Throwable throwableCauseTransientPropertyValue = returnCauseTransientPropertyValueException(e);
		String title = messageUtils.getMessageEnglish("invalid.data.access.api.usage");
		String mensagem = e.getMessage();
		String details = StringUtils.EMPTY;
		if (throwableCauseTransientPropertyValue != null) {
			title = messageUtils.getMessageEnglish("transient.property.exception.title");
			mensagem = messageUtils.getMessageEnglish("transient.property.exception",new String[] {((TransientPropertyValueException)throwableCauseTransientPropertyValue).getPropertyName()});
			details = messageUtils.getMessageEnglish("transient.property.exception.details");
		}
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), title,
				mensagem, details, ((ServletWebRequest) request).getRequest().getRequestURL().toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);

	}

	private Throwable returnCauseTransientPropertyValueException(InvalidDataAccessApiUsageException e) {
		Throwable c = e;
		while (c.getCause() != null) {
			if (c.getCause() instanceof TransientPropertyValueException) {
				return c.getCause();
			}
			c = c.getCause();
		}
		return null;
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ResponseEntity<StandardError> authorization(AccessDeniedException e, HttpServletRequest request) {
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(),
				messageUtils.getMessageEnglish("authorization.exception.title"),
				messageUtils.getMessageEnglish("authorization.exception"),
				messageUtils.getMessageEnglish("authorization.exception.details"),
				(request.getRequestURL().toString()));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
}
