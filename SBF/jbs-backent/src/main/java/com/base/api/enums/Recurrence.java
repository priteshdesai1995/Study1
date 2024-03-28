package com.base.api.enums;

public enum Recurrence {
	
	NEVER("NEVER"),DAILY("DAILY");

	private String type;

	private Recurrence(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
