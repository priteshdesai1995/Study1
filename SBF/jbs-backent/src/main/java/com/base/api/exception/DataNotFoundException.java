package com.base.api.exception;

import org.apache.http.HttpStatus;

public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -4851850288139617899L;
	private static HttpStatus status = null;

	public DataNotFoundException(String message) {
		super(message);
		DataNotFoundException.status = null;
	}

	public DataNotFoundException(String message, HttpStatus status) {
		super(message);
		DataNotFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return DataNotFoundException.status;
	}

}
