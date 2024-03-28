package com.humaine.portal.api.enums;

public enum DurationGraphTimeUnit {

	MILLISECONDS("MILLISECONDS"), 
	SECONDS("SECONDS"), 
	MINUTES("MINUTES");

	public String unit;

	DurationGraphTimeUnit() {
	}

	DurationGraphTimeUnit(String unit) {
		this.unit = unit;
	}

	public String value() {
		return unit;
	}
}
