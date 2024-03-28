/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.enums;

/**
 * This class contains enums that represent constant values for Platform.
 * 
 * @author minesh_prajapati
 *
 */
public enum Platform {

	ALL("All"), ANDROID("Android"), IOS("Ios"), WEB("Web");

	private String type;

	private Platform(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
