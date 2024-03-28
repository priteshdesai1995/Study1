package com.base.api.exception;

import org.apache.http.HttpStatus;

public class PlanNotFoundException extends RuntimeException {
		

	private static final long serialVersionUID = 1232841755334210287L;
	private static HttpStatus status = null;

	public PlanNotFoundException(String message) {
		super(message);
		PlanNotFoundException.status = null;
	}

	public PlanNotFoundException(String message, HttpStatus status) {
		super(message);
		PlanNotFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return PlanNotFoundException.status;
	}

}
