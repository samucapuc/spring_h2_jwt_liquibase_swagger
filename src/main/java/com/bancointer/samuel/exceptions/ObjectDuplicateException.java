package com.bancointer.samuel.exceptions;

public class ObjectDuplicateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ObjectDuplicateException(String msg) {
		super(msg);
	}
	
	public ObjectDuplicateException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
