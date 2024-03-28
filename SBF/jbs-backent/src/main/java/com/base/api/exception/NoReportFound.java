package com.base.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author preyansh_prajapati
 * Custom exception for the tokenNotFound
 */
public class NoReportFound extends RuntimeException {

	private static final long serialVersionUID = 8694972023863631260L;
	
	private static HttpStatus status = null;
	
	public NoReportFound(String message) {
		super(message);
		NoReportFound.status = null;
	}

	public NoReportFound(String message, HttpStatus status) {
		super(message);
		NoReportFound.status = status;
	}
	
	public static HttpStatus getStatus() {
		return NoReportFound.status;
	}
	
}
