package com.humaine.portal.api.exception;

public class EmailClientException extends RuntimeException {

	public EmailClientException(String errorMessage, Throwable throwable) {
		super(errorMessage, throwable);
	}

}