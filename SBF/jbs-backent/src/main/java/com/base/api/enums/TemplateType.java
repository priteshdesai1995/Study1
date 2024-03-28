package com.base.api.enums;

public enum TemplateType {

	EMAIL("EMAIL"), PUSH("PUSH"), SMS("SMS");

	private String type;

	private TemplateType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
