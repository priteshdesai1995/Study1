package com.humaine.admin.api.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.humaine.admin.api.util.ErrorField;
import com.humaine.admin.api.util.ErrorMessageBuilder;
import com.humaine.admin.api.util.ErrorStatus;
import com.humaine.admin.api.util.TransactionInfo;

@ControllerAdvice
@RestController
public class ExceptionController extends ResponseEntityExceptionHandler {

	private static final Logger log = LogManager.getLogger(ExceptionController.class);

	@ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
	private ResponseEntity<TransactionInfo> exception(UnsatisfiedServletRequestParameterException exception) {
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		log.error("{} {}", ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage()));
		transactionInfo.addErrorList(new ErrorField(ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage())));
		transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(APIException.class)
	private ResponseEntity<TransactionInfo> handleAPIexception(APIException exception) {
		log.error("{} {}", ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage()));
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(new ErrorField(ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage())));
		transactionInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccessDeniedException.class)
	private ResponseEntity<TransactionInfo> handleAccessDeniedException(AccessDeniedException exception) {
		log.error("{} {}", ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage()));
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(new ErrorField(ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage())));
		transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	private ResponseEntity<TransactionInfo> exception(Exception exception) {
		log.error("{} {}", ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage()));
		exception.printStackTrace();
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(new ErrorField(ErrorMessageBuilder.retriveCode(exception.getMessage()),
				ErrorMessageBuilder.retriveMessage(exception.getMessage())));
		transactionInfo.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/*
	 * @ExceptionHandler(OAuth2AccessDeniedException.class) private
	 * ResponseEntity<TransactionInfo>
	 * accessDeniedExcpetion(OAuth2AccessDeniedException exception) {
	 * TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(exception.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN); }
	 * 
	 * @ExceptionHandler(OAuth2AuthorizationException.class) private
	 * ResponseEntity<TransactionInfo>
	 * oAuth2AuthorizationException(OAuth2AuthorizationException exception) {
	 * TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(exception.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(OAuth2AuthenticationException.class) private
	 * ResponseEntity<TransactionInfo>
	 * oAuth2AuthenticationException(OAuth2AuthenticationException exception) {
	 * TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(exception.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(InternalAuthenticationServiceException.class) private
	 * ResponseEntity<TransactionInfo> internalAuthenticationServiceException(
	 * InternalAuthenticationServiceException exception) { TransactionInfo
	 * transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(exception.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(CommonException.class) private
	 * ResponseEntity<TransactionInfo> commonException(CommonException ex) {
	 * TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(ex.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(RuntimeException.class) private
	 * ResponseEntity<TransactionInfo> runtimeException(RuntimeException ex) {
	 * TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(ex.getMessage()); if (ex instanceof
	 * AccessDeniedException) {
	 * transactionInfo.setStatusCode(HttpStatus.FORBIDDEN.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.FORBIDDEN); } return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 * 
	 * @ExceptionHandler(UsernameNotFoundException.class) private
	 * ResponseEntity<TransactionInfo> runtimeException(UsernameNotFoundException
	 * ex) { TransactionInfo transactionInfo = new TransactionInfo(false);
	 * transactionInfo.addErrorList(ex.getMessage());
	 * transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value()); return new
	 * ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST); }
	 */

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
		ex.getBindingResult().getFieldErrors().forEach(fe -> {
			transactionInfo
					.addErrorList(new ErrorField(ErrorMessageBuilder.retriveCode(fe.getDefaultMessage(), fe.getCode()),
							ErrorMessageBuilder.retriveMessage(fe.getDefaultMessage())));
		});
		log.error(transactionInfo);
		return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
	}

}
