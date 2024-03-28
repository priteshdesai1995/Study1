package com.humaine.collection.api.util;

public enum ErrorStatus {
	SUCCESS("SUCCESS"), FAIL("FAIL");

	public String status;

	ErrorStatus() {
	}

	ErrorStatus(String status) {
		this.status = status;
	}

	public String value() {
		return status;
	}
}
