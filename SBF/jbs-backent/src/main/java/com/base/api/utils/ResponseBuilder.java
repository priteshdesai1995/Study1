/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.utils;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.base.api.entities.Event;

/**
 * 
 * This is the class that create the response for the APIs
 * 
 * @author preyansh_prajapati
 *
 *         This is the class that create the response for the APIs
 */
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

	public static ResponseEntity<TransactionInfo> buildOkResponse(Object result) {

		return buildResponse(ErrorStatus.SUCCESS, "", ResponseStatus.SUCCESS, HttpStatus.OK, HttpStatus.OK, "", result);

	}

	public static ResponseEntity<TransactionInfo> buildStatusChangeResponse(Object result, String message) {

		return buildResponse(ErrorStatus.SUCCESS, message, ResponseStatus.SUCCESS, HttpStatus.OK, HttpStatus.OK, "",
				result);

	}

	public static ResponseEntity<TransactionInfo> buildCRUDResponse(Object result, String message, HttpStatus status) {

		return buildResponse(ErrorStatus.SUCCESS, message, ResponseStatus.SUCCESS, HttpStatus.OK, status, "", result);

	}

	public static ResponseEntity<TransactionInfo> buildInternalServerErrorResponse(Object result, String message) {

		return buildResponse(ErrorStatus.SUCCESS, message, ResponseStatus.FAIL, HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR, message, result);

	}

	public static ResponseEntity<TransactionInfo> buildRecordNotFoundResponse(Object result, String message) {

		return buildResponse(ErrorStatus.FAIL, message, ResponseStatus.FAIL, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND,
				message, result);

	}

	public static ResponseEntity<TransactionInfo> buildSearchFileterResponse(Object result, String message, int count) {

		return buildSearchResponse(ErrorStatus.SUCCESS, message, ResponseStatus.SUCCESS, HttpStatus.OK, HttpStatus.OK,
				"", result, count);

	}

	public static ResponseEntity<TransactionInfo> buildWithMessage(String msg) {
		return buildResponse(ErrorStatus.SUCCESS, msg, ResponseStatus.SUCCESS, HttpStatus.OK, HttpStatus.OK, "", null);
	}

	public static ResponseEntity<TransactionInfo> buildWithMessageAndStatus(String msg, HttpStatus status) {
		return buildResponse(ErrorStatus.SUCCESS, msg, ResponseStatus.SUCCESS, status, status, null, null);
	}

	public static ResponseEntity<TransactionInfo> buildResponse(ErrorStatus status, String message,
			ResponseStatus responseStatus, HttpStatus metaStatusCode, HttpStatus transactionStatusCode,
			String errorList, Object data) {

		MetaResponse metaResponse = new MetaResponse(status.value());
		metaResponse.setMessage(message);
		metaResponse.setMessageCode(responseStatus.value());
		metaResponse.setStatusCode(metaStatusCode.value());

		TransactionInfo transactionInfo = new TransactionInfo(status);
		transactionInfo.setStatusCode(transactionStatusCode.value());
		transactionInfo.addErrorList(errorList);
		transactionInfo.setResponseData(data);
		transactionInfo.setMeta(metaResponse);

		return new ResponseEntity<TransactionInfo>(transactionInfo, transactionStatusCode);
	}

	/**
	 * Builds the search response.
	 *
	 * @param status                the status
	 * @param message               the message
	 * @param responseStatus        the response status
	 * @param metaStatusCode        the meta status code
	 * @param transactionStatusCode the transaction status code
	 * @param errorList             the error list
	 * @param data                  the data
	 * @param count                 the count
	 * @return the response entity
	 */
	public static ResponseEntity<TransactionInfo> buildSearchResponse(ErrorStatus status, String message,
			ResponseStatus responseStatus, HttpStatus metaStatusCode, HttpStatus transactionStatusCode,
			String errorList, Object data, int count) {
		MetaResponse metaResponse = new MetaResponse();
		metaResponse.setMessage(message);
		metaResponse.setMessageCode(responseStatus.value());
		metaResponse.setStatus(status.value());
		metaResponse.setStatusCode(transactionStatusCode.value());
		metaResponse.setCount(count);

		TransactionInfo transactionInfo = new TransactionInfo();
		transactionInfo.setStatusCode(transactionStatusCode.value());
		transactionInfo.addErrorList(errorList);
		transactionInfo.setResponseData(data);
		transactionInfo.setMeta(metaResponse);

		return new ResponseEntity<TransactionInfo>(transactionInfo, transactionStatusCode);
	}

	/***
	 * 
	 * Build API response with only code and message in Response Data
	 * 
	 */
//
//	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg) {
//		return buildMessageCodeResponse(ErrorMessageBuilder.retriveMessage(msg), ErrorMessageBuilder.retriveCode(msg));
//	}

//
//	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg, String code) {
//		return buildMessageCodeResponse(msg, code, HttpStatus.OK);
//	}
//
//	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg, String code, HttpStatus status) {
//		ErrorField field = new ErrorField(code, msg);
//		TransactionInfo body = new TransactionInfo(ResponseStatus.SUCCESS, field, status.value(),
//				new ArrayList<ErrorField>());
//		return new ResponseEntity<TransactionInfo>(body, status);
//	}

	public static ResponseEntity<TransactionInfo> buildMessageCodeResponse(String msg, String code, HttpStatus status) {
		ErrorField field = new ErrorField(code, msg);
		TransactionInfo body = new TransactionInfo(ErrorStatus.SUCCESS, field, status.value(),
				new ArrayList<ErrorField>());
		return new ResponseEntity<TransactionInfo>(body, status);
	}
}
