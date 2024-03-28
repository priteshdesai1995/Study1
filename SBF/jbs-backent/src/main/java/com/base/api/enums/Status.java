/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.enums;

/**
 * This class contains enums that represent constant values for Status.
 * 
 * @author minesh_prajapati
 *
 */
public enum Status {

	PENDING("Pending"), INPROGRESS("In-Progress"), ANNOUNCED("Announced");

	private String type;

	private Status(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
