package com.base.api.exception;

import org.springframework.http.HttpStatus;

public class NoActiveStatusFoundException extends RuntimeException
 {

	private static final long serialVersionUID = 8694972023863631260L;
	
	private static HttpStatus status = null;
	
	public NoActiveStatusFoundException(String message) {
		super(message);
		NoActiveStatusFoundException.status = null;
	}

	public NoActiveStatusFoundException(String message, HttpStatus status) {
		super(message);
		NoActiveStatusFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return NoActiveStatusFoundException.status;
	}
	
	
}
