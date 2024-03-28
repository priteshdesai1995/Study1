package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.util.CommonUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyJourneyAnalysisResponse {

	Long id;

	Long groupId;

	Long journeySteps;

	Long totalJourneySteps;

	String journeyTime;

	Float successRate;

	String groupName;

	String bigFive;

	String firstInterest;

	String decison;

	String purchaseAddCart;

	String purchaseBuy;

	String purchaseOwnership;

	public MyJourneyAnalysisResponse(MyJourneyDBResponse journey) {
		super();
		this.id = journey.getId();
		this.groupId = journey.getGroupId();
		this.journeyTime = CommonUtils.formatTime(journey.getJourneyTime());
		this.successRate = CommonUtils.format(journey.getJourneySuccess());
		this.journeySteps = journey.getJourneySteps();
		this.decison = journey.getDecison();
		this.firstInterest = journey.getFirstInterest();
		this.purchaseAddCart = journey.getPurchaseAddCart();
		this.purchaseBuy = journey.getPurchaseBuy();
		this.purchaseOwnership = journey.getPurchaseOwnership();
	}

}
