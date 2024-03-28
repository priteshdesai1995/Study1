package com.base.api.enums;

public enum PlanType {
	TRIAL("TRIAL"),PAID("PAID");
	

	private String planType;

	private PlanType(String planType) {
		this.planType = planType;
	}

	public String getPlanType() {
		return planType;
	}
}
