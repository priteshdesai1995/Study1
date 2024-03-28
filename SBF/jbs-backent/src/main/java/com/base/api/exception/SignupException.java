package com.base.api.exception;

import org.springframework.http.HttpStatus;

public class SignupException extends RuntimeException {

	private static final long serialVersionUID = -7263397231008115685L;

	private static HttpStatus status = null;

	public SignupException(String message) {
		super(message);
		SignupException.status = null;
	}

	public SignupException(String message, HttpStatus status) {
		super(message);
		SignupException.status = status;
	}

	public static HttpStatus getStatus() {
		return SignupException.status;
	}
}
