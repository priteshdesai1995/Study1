package com.base.api.exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

	private static final long serialVersionUID = 7175184849141858644L;

	private static HttpStatus status = null;

	public APIException(String message) {
		super(message);
		APIException.status = null;
	}

	public APIException(String message, HttpStatus status) {
		super(message);
		APIException.status = status;
	}
	
	public static HttpStatus getHttpStatus() {
		return APIException.status;
	}
}
