package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati
 * Custom exception for the tokenNotFound
 */
public class TokenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8694972023863631260L;
	
	private static HttpStatus status = null;
	
	public TokenNotFoundException(String message) {
		super(message);
		TokenNotFoundException.status = null;
	}

	public TokenNotFoundException(String message, HttpStatus status) {
		super(message);
		TokenNotFoundException.status = status;
	}
	
	public static HttpStatus getStatus() {
		return TokenNotFoundException.status;
	}
	
}
