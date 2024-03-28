package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati Custom exception for the tokenNotFound
 */
public class PasswordException extends RuntimeException {

	private static final long serialVersionUID = -1633011798436079061L;
	private static HttpStatus status = null;

	public PasswordException(String message) {
		super(message);
		PasswordException.status = null;
	}

	public PasswordException(String message, HttpStatus status) {
		super(message);
		PasswordException.status = status;
	}

	public static HttpStatus getStatus() {
		return PasswordException.status;
	}

}
