/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.enums;

/**
 * This class contains enums that represent constant values for Inclusion.
 * 
 * @author minesh_prajapati
 *
 */
public enum Inclusion {

	ALL("All"), EXCLUDE("Exclude"), INCLUDE("Include");

	private String type;

	private Inclusion(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
