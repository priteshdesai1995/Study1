package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati 
 */
public class PermissionException extends RuntimeException {

	private static final long serialVersionUID = 1204351142079091377L;
	private static HttpStatus status = null;

	public PermissionException(String message) {
		super(message);
		PermissionException.status = null;
	}

	public PermissionException(String message, HttpStatus status) {
		super(message);
		PermissionException.status = status;
	}

	public static HttpStatus getStatus() {
		return PermissionException.status;
	}

}
