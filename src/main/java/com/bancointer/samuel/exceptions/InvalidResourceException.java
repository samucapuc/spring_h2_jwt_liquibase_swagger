package com.bancointer.samuel.exceptions;

public class InvalidResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidResourceException(String msg) {
		super(msg);
	}
	
	public InvalidResourceException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
