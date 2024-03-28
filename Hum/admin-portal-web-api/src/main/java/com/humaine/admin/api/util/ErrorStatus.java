package com.humaine.admin.api.util;

public enum ErrorStatus {

	SUCCESS("SUCCESS"), FAIL("FAIL");

	public String status;

	ErrorStatus() {
		// TODO Auto-generated constructor stub
	}

	ErrorStatus(String status) {
		this.status = status;
	}

	public String value() {
		return status;
	}
}
