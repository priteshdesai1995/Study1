/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.base.api.utils.ErrorStatus;
import com.base.api.utils.TransactionInfo;
import com.base.api.utils.Translator;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is use to handle exception.
 * 
 * @author minesh_prajapati
 *
 */
@ControllerAdvice
@RestController
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
	private ResponseEntity<TransactionInfo<String>> exception(
			UnsatisfiedServletRequestParameterException exception) {
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		log.error(exception.getMessage());
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidGrantException.class)
	public ResponseEntity<TransactionInfo<String>> handleInternalAuthenticationServiceException(
			APIException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<TransactionInfo<String>> handleAccessDenide(
			AccessDeniedException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(APIException.class)
	public ResponseEntity<TransactionInfo<String>> handleAPIexception(APIException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo
				.setStatusCode(exception.getHttpStatus() != null ? exception.getHttpStatus().value()
						: HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * This is the exception handler for UserNotFoundException.
	 * 
	 * @param exception
	 * @return ResponseEntity
	 */
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> handleUserNotFoundException(
			UserNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * @param exception
	 * @return
	 * 
	 *         this is the exception handler for handleTokenNotFoundException
	 */
	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> handleTokenNotFoundException(
			TokenNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PasswordException.class)
	public ResponseEntity<TransactionInfo<String>> handlePasswordException(
			PasswordException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	private ResponseEntity<TransactionInfo<String>> exception(Exception exception) {
		log.error(exception.getMessage());
		exception.printStackTrace();
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);


	
	}
	
	@ExceptionHandler(PlanNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> PlanNotFoundException(PlanNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(exception.getMessage());
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(QuestionsNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> QuestionsNotFoundException(QuestionsNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(exception.getMessage());
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}
	
	
	@ExceptionHandler(NoActiveStatusFoundException.class)
	public ResponseEntity<TransactionInfo<String>> NoActiveStatusFoundException(NoActiveStatusFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(exception.getMessage());
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}
	
	
	
	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> EventNotFoundException(EventNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(exception.getMessage());
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<TransactionInfo<String>> DataNotFoundException(DataNotFoundException exception) {
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(exception.getMessage());
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		ex.getBindingResult().getFieldErrors().forEach(fe -> {
			transactionInfo.addErrorList(Translator.toLocale(fe.getDefaultMessage()));
		});
		log.error(transactionInfo.toString());
		return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserRoleException.class)
	public ResponseEntity<TransactionInfo<String>> handleUserRoleException(UserRoleException exception){
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.NOT_FOUND.value());	
		return new ResponseEntity<>(transactionInfo, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PermissionException.class)
	public ResponseEntity<TransactionInfo<String>> handlePermissionException(PermissionException exception){
		log.error(exception.getMessage());
		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL);
		transactionInfo.addErrorList(Translator.toLocale(exception.getMessage()));
		transactionInfo.setStatusCode(HttpStatus.NOT_FOUND.value());	
		return new ResponseEntity<>(transactionInfo, HttpStatus.NOT_FOUND);
	}

}
