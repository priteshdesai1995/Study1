package com.base.api.utils;

public enum ResponseStatus {
	SUCCESS(true), FAIL(false);

	public boolean status;

	ResponseStatus() {
	}

	ResponseStatus(boolean status) {
		this.status = status;
	}

	public boolean value() {
		return status;
	}
}

