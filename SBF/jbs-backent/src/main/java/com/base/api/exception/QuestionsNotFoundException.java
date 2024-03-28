package com.base.api.exception;

import org.apache.http.HttpStatus;

public class QuestionsNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -4851850288139617899L;
	private static HttpStatus status = null;

	public QuestionsNotFoundException(String message) {
		super(message);
		QuestionsNotFoundException.status = null;
	}

	public QuestionsNotFoundException(String message, HttpStatus status) {
		super(message);
		QuestionsNotFoundException.status = status;
	}

	public static HttpStatus getStatus() {
		return QuestionsNotFoundException.status;
	}

}
