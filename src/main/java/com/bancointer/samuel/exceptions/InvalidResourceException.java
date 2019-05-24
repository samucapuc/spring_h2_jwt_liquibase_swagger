package com.bancointer.samuel.exceptions;

import org.springframework.web.bind.annotation.RequestMethod;


public class InvalidResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private RequestMethod httpMethod;
	
	public InvalidResourceException(String msg) {
		super(msg);
	}
	
	public InvalidResourceException(String msg, RequestMethod httpMethod) {
		super(msg);
		this.setHttpMethod(httpMethod);
	}
	
	public InvalidResourceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RequestMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(RequestMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

}
