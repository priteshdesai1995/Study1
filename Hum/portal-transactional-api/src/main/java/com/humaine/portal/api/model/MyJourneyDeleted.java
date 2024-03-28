package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.olap.model.OLAPMyJourney;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "my_journey_deleted")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MyJourneyDeleted {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "journey_id", columnDefinition = "bigint")
	Long journeyId;

	@Column(name = "journey_journey_id", columnDefinition = "bigint")
	Long journeyParentId;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

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

	@Column(name = "journey_created_on")
	OffsetDateTime createDate;

	@Column(name = "created_on")
	OffsetDateTime timestamp;

	@Column(name = "journey_success")
	Float journeySuccess;

	@Column(name = "journey_time")
	Float journeyTime;

	public MyJourneyDeleted(OLAPMyJourney journey, OffsetDateTime timestemp) {
		this.journeyId = journey.getId();
		this.journeyParentId = journey.getJourneyId();
		this.account = journey.getAccount();
		this.groupId = journey.getGroupId();
		this.firstInterest = journey.getFirstInterest();
		this.decison = journey.getDecison();
		this.purchaseAddCart = journey.getPurchaseAddCart();
		this.purchaseBuy = journey.getPurchaseBuy();
		this.purchaseOwnership = journey.getPurchaseOwnership();
		this.journeySteps = journey.getJourneySteps();
		this.createDate = journey.getCreateDate();
		this.journeySuccess = journey.getJourneySuccess();
		this.journeyTime = journey.getJourneyTime();
		this.timestamp = timestemp;
	}
}
