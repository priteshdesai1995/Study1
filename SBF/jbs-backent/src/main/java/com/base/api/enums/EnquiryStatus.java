package com.base.api.enums;

public enum EnquiryStatus {
	
	SUCCESS("SUCCESS"), ERROR("ERROR"), PENDING("PENDING"), ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), DELETED("DELETED"),
	APPROVED("APPROVED");

	private String status;

	EnquiryStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
