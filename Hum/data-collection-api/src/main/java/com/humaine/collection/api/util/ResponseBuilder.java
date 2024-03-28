package com.humaine.collection.api.util;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseBuilder {

	/***
	 * 
	 * Build API response with Generic Type
	 * 
	 */
	public static ResponseEntity<Object> build() {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	public static <T> ResponseEntity<T> build(T resp) {
		return build(resp, HttpStatus.OK);
	}

	public static <T> ResponseEntity<T> build(T resp, HttpStatus status) {
		return new ResponseEntity<T>(resp, status);
	}

	/***
	 * 
	 * Build API response with Generic TransactionInfo
	 * 
	 */
	public static ResponseEntity<TransactionInfo> buildResponse(Object resp) {
		return buildResponse(resp, HttpStatus.OK);
	}

	public static ResponseEntity<TransactionInfo> buildResponse(Object resp, HttpStatus status) {
		return buildResponse(resp, status, ErrorStatus.SUCCESS);
	}

	public static ResponseEntity<TransactionInfo> buildResponse(Object resp, HttpStatus httpCode, ErrorStatus status) {
		TransactionInfo body = new TransactionInfo(status, resp, httpCode.value(), new ArrayList<ErrorField>());
		return new ResponseEntity<TransactionInfo>(body, httpCode);
	}

	/***
	 * 
	 * Build API response with only code and message in Response Data
	 * 
	 */

	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg) {
		return buildMessageCodeResponse(ErrorMessageBuilder.retriveMessage(msg), ErrorMessageBuilder.retriveCode(msg));
	}

	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg, String code) {
		return buildMessageCodeResponse(msg, code, HttpStatus.OK);
	}

	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg, String code, HttpStatus status) {
		ErrorField field = new ErrorField(code, msg);
		TransactionInfo body = new TransactionInfo(ErrorStatus.SUCCESS, field, status.value(),
				new ArrayList<ErrorField>());
		return new ResponseEntity<TransactionInfo>(body, status);
	}
}
