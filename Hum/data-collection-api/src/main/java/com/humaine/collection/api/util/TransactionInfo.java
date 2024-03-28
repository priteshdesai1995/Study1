package com.humaine.collection.api.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionInfo implements Serializable {

	private static final long serialVersionUID = -5920854604468221743L;

	private ErrorStatus status;
	private Object responseData;
	private Integer statusCode;
	private List<ErrorField> errorList = new ArrayList<ErrorField>();

	public TransactionInfo(ErrorStatus status) {
		this.status = status;
	}

	public void addErrorList(ErrorField errField) {
		if (errorList == null)
			errorList = new ArrayList<ErrorField>();

		errorList.add(errField);
	}

	@Override
	public String toString() {
		return "TransactionInfo [status=" + status + ", responseData=" + responseData + ", statusCode=" + statusCode
				+ ", errorList=" + errorList + "]";
	}

}
