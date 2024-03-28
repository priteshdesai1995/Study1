package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.model.TestJourney;

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
public class TestJourneyResponse {

	Long id;

	Long groupId;

	String firstInterest;

	String decison;

	String purchaseAddCart;

	String purchaseBuy;

	String purchaseOwnership;

	Long journeySteps;

	Long totalJourneySteps;

	String journeyTime;

	Float successRate;

	String groupName;

	String bigFive;

	public TestJourneyResponse(TestJourney journey, Long totalSteps) {
		this.id = journey.getId();
		this.groupId = journey.getGroupId();
		this.firstInterest = journey.getFirstInterest();
		this.decison = journey.getDecison();
		this.purchaseAddCart = journey.getPurchaseAddCart();
		this.purchaseBuy = journey.getPurchaseBuy();
		this.purchaseOwnership = journey.getPurchaseOwnership();
		this.journeySteps = journey.getJourneySteps();
		this.totalJourneySteps = totalSteps;
	}

	public TestJourneyResponse(JournyResponse journey, Long totalSteps) {
		this(journey.getJourney(), totalSteps);
		this.bigFive = journey.getBigFive();
		this.successRate = journey.getSuccessMatch();
		this.journeyTime = journey.getJourneyTime();
		this.groupName = journey.getGroupName();
	}
}
