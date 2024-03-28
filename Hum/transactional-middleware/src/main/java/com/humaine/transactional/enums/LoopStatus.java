package com.humaine.transactional.enums;

public enum LoopStatus {
	COMPLETED("COMPLETED"), CONTINUE("CONTINUE");

	public String status;

	LoopStatus() {
	}

	LoopStatus(String status) {
		this.status = status;
	}

	public String value() {
		return status;
	}
}
