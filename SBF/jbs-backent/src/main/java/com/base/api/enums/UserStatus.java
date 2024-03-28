/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.enums;

import java.util.Arrays;

/**
 * This class contains enums that represent constant values for Permission..
 * 
 * @author jay_patel
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
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

	/**
	 * This method convert string status to enum status.
	 * 
	 * @param statusName
	 * @return enum of UserStatus
	 * @throws IllegalArgumentException
	 */

	public static UserStatus valueOfCode(String statusName) throws IllegalArgumentException {
		UserStatus userStatus = Arrays.stream(UserStatus.values())
				.filter(val -> val.name().equalsIgnoreCase(statusName)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unable To Resolve Status: " + statusName));
		return userStatus;
	}
}
