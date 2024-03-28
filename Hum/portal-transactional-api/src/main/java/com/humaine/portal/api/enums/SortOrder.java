package com.humaine.portal.api.enums;

public enum SortOrder {

	ASC("ASC"), DESC("ASC");

	public String order;

	SortOrder() {
	}

	SortOrder(String value) {
		this.order = value;
	}

	public String value() {
		return order;
	}
}
