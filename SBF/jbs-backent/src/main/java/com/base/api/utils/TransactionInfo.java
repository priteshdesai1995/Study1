
/**
 * Copyright 2022 Brainvire - All rights reserved.
 */

package com.base.api.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TransactionInfo<T extends Object> implements Serializable {

	private static final long serialVersionUID = 1062288418223256691L;

	private ErrorStatus status;

	private Integer statusCode;

	private HttpStatus httpStatus;

	private String successMessage;

	private List<String> errorList = new ArrayList<String>();

	private MetaResponse meta;

	private PageableResponse page;

	private T responseData;

	private String stringResponse;

	private int count;

	public TransactionInfo(ErrorStatus status) {
		this.status = status;
	}

	public void addErrorList(String err) {
		if (errorList == null)
			errorList = new ArrayList<String>();

		errorList.add(err);
	}

	public TransactionInfo(ErrorStatus status, Integer statusCode, List<String> errorList, T responseData) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.errorList = errorList;
		this.responseData = responseData;
	}

	public TransactionInfo(String status) {
		List<String> errorList = new ArrayList<>();
		errorList.add(status);
		this.errorList = errorList;
	}

	public TransactionInfo(ErrorStatus status, T body, int value, ArrayList<String> arrayList) {
		this.status = status;
		this.responseData = body;
		this.statusCode = value;
		this.errorList = arrayList;
	}

	public TransactionInfo(ErrorStatus status, int statusValue, List<String> errorList, T body, int count) {
		this.status = status;
		this.responseData = body;
		this.statusCode = statusValue;
		this.errorList = errorList;
		this.count = count;
	}
}
