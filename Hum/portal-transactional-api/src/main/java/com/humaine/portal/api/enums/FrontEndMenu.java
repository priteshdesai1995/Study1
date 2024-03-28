package com.humaine.portal.api.enums;

public enum FrontEndMenu {

	TEST_NEW_JOURNEY("TEST_NEW_JOURNEY");

	public String menu;

	FrontEndMenu() {
	}

	FrontEndMenu(String menu) {
		this.menu = menu;
	}

	public String value() {
		return menu;
	}
}
