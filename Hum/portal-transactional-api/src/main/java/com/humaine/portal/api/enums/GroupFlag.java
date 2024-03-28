package com.humaine.portal.api.enums;

public enum GroupFlag {
	AI_GENERATED(1), 
	MY_USER_GROUP(2),
	MANUAL(3);

	public Integer flag;

	GroupFlag() {
	}

	GroupFlag(Integer value) {
		this.flag = value;
	}

	public Integer value() {
		return flag;
	}
}
