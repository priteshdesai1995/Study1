package com.base.api.enums;

public enum PlanStatus {
	
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE");
	
	private String planStatus;
	
	private PlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getPlanStatus() {
		return planStatus;
	}
	
	
}
