package com.humaine.portal.api.enums;

public enum AccountStatus {
	confirmed("confirmed"), unconfirmed("unconfirmed");

	public String status;

	AccountStatus() {
	}

	AccountStatus(String status) {
		this.status = status;
	}

	public String value() {
		return status;
	}

}
