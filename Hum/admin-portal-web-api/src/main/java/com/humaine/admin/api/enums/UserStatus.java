package com.humaine.admin.api.enums;

public enum UserStatus {
	SUCCESS("Success"), ERROR("Error"), PENDING("Pending"), ACTIVE("Active"), INACTIVE("Inactive"), DELETED("Deleted"),
	APPROVED("Approved");

	private String status;

	UserStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
