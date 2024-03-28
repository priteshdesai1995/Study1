package com.base.api.enums;

public enum SurveyStatus {
	
	Pending("Pending"),Inprogress("Inprogress"),Completed("Completed") , Isempty("Isempty");
	
	private String status;

	SurveyStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
