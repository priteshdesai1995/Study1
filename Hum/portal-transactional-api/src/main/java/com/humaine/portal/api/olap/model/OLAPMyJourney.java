package com.humaine.portal.api.olap.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "my_journey")
@NamedNativeQuery(name = "getAllJourneys", query = "SELECT j FROM my_journey j WHERE j.account_id=:accountID and j.created_on  = (select max(mj.created_on) from my_journey mj where mj.account_id =:accountID) order by j.journey_success desc", resultClass = OLAPMyJourney.class)
public class OLAPMyJourney {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@Column(name = "account_id", nullable = false)
	@JsonIgnore
	Long account;

	@Column(name = "group_id", nullable = false)
	Long groupId;

	@Column(name = "journey_id", nullable = false)
	Long journeyId;

	@Column(name = "journey_success")
	Float journeySuccess;

	@Column(name = "journey_time")
	Float journeyTime;

	@Column(name = "created_on")
	OffsetDateTime createDate;

	@Column(name = "journey_steps")
	Long journeySteps;

	@Column(name = "first_interest", length = 255)
	String firstInterest;

	@Column(name = "decision", length = 255)
	String decison;

	@Column(name = "purchase_add_cart", length = 255)
	String purchaseAddCart;

	@Column(name = "purchase_buy", length = 255)
	String purchaseBuy;

	@Column(name = "purchase_ownership", length = 255)
	String purchaseOwnership;

}
