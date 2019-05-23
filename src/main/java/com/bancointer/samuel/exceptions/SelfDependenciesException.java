package com.bancointer.samuel.exceptions;

public class SelfDependenciesException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SelfDependenciesException(String msg) {
		super(msg);
	}
	
	public SelfDependenciesException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
