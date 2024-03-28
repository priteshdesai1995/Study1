package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.request.dto.CreateTestJourneyRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "test_journey_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TestJourney {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountid", nullable = false)
	@JsonIgnore
	Account account;

	@Column(name = "group_id", columnDefinition = "bigint")
	Long groupId;

	@Column(name = "first_interest", length = 255)
	String firstInterest;

	@Column(name = "decison", length = 255)
	String decison;

	@Column(name = "purchase_add_cart", length = 255)
	String purchaseAddCart;

	@Column(name = "purchase_buy", length = 255)
	String purchaseBuy;

	@Column(name = "purchase_ownership", length = 255)
	String purchaseOwnership;

	@Column(name = "journey_steps")
	Long journeySteps;

	@Column(name = "created_on")
	OffsetDateTime createdOn;

	public TestJourney(CreateTestJourneyRequest request, Long filledSteps, Account acc, OffsetDateTime timestemp) {
		this.updateFileds(request, filledSteps, acc, timestemp);
	}
	
	public void updateFileds(CreateTestJourneyRequest request, Long filledSteps, Account acc, OffsetDateTime timestemp) {
		this.account = acc;
		this.groupId = request.getGroupId();
		this.firstInterest = request.getFirstInterest();
		this.decison = request.getDecison();
		this.purchaseAddCart = request.getPurchaseAddCart();
		this.purchaseBuy = request.getPurchaseBuy();
		this.purchaseOwnership = request.getPurchaseOwnership();
		this.journeySteps = filledSteps;
		this.createdOn = timestemp;
	}
	
	public TestJourney(Long id, Long groupId, Account account) {
		this.id = id;
		this.groupId = groupId;
		this.account = account;
	}
}
