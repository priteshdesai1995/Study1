package com.humaine.portal.api.enums;

public enum Genders {

	MALE("Male"), FEMALE("Female"), OTHER("Other");

	public String gender;

	Genders() {
	}

	Genders(String gender) {
		this.gender = gender;
	}

	public String value() {
		return gender;
	}
}
