package com.base.api.exception;

import org.apache.http.HttpStatus;

public class EventNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1232841755334210287L;
	private static HttpStatus status = null;

	public EventNotFoundException(String message) {
		super(message);
		EventNotFoundException.status = null;
	}

	public EventNotFoundException(String message, HttpStatus status) {
		super(message);
		EventNotFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return EventNotFoundException.status;
	}

}