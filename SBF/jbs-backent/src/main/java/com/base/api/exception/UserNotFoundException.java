package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati
 * 
 * This is custom exception for user not found 
 *
 */
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1540322300985695749L;

	private static HttpStatus status = null;

	public UserNotFoundException(String message) {
		super(message);
		UserNotFoundException.status = null;
	}

	public UserNotFoundException(String message, HttpStatus status) {
		super(message);
		UserNotFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return UserNotFoundException.status;
	}
}
