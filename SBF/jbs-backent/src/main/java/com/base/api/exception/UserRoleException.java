package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati 
 */
public class UserRoleException extends RuntimeException {

	private static final long serialVersionUID = 1204351142079091377L;
	private static HttpStatus status = null;

	public UserRoleException(String message) {
		super(message);
		UserRoleException.status = null;
	}

	public UserRoleException(String message, HttpStatus status) {
		super(message);
		UserRoleException.status = status;
	}

	public static HttpStatus getStatus() {
		return UserRoleException.status;
	}

}
