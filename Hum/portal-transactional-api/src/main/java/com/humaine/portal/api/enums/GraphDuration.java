package com.humaine.portal.api.enums;

public enum GraphDuration {

	TWENTY_FOUR_HOURS("TWENTY_FOUR_HOURS"), FORTY_EIGHT_HOURS("FORTY_EIGHT_HOURS"), ONE_WEEK("ONE_WEEK");

	public String duration;

	GraphDuration() {
	}

	GraphDuration(String duration) {
		this.duration = duration;
	}

	public String value() {
		return duration;
	}
}
