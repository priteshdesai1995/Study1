package com.base.api.utils;


public enum ErrorStatus {
	SUCCESS(true), FAIL(false);

	public boolean status;

	ErrorStatus() {
	}
	ErrorStatus(boolean status) {
		this.status = status;
	}

	public boolean value() {
		return status;
	}
}

