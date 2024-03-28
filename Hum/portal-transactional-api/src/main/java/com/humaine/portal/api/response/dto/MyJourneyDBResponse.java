package com.humaine.portal.api.response.dto;

public interface MyJourneyDBResponse {

	Long getId();

	Long getAccount();

	Long getGroupId();

	Long getJourneySteps();

	Float getJourneySuccess();

	Float getJourneyTime();

	String getFirstInterest();

	String getDecison();

	String getPurchaseAddCart();

	String getPurchaseBuy();

	String getPurchaseOwnership();

}
