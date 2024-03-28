package com.humaine.admin.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accountdetails")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BusinessInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountdetailid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "full_name", length = 255)
	String name;

	@Column(name = "url", length = 255)
	String url;

	@Column(name = "phonenumber", length = 32)
	String phoneNumber;

	@Column(name = "address", length = 255)
	String address;

	@Column(name = "headquarter_location", length = 255)
	String headquarterLocation;

	@Column(name = "highest_product_price")
	String highestProductPrice;

	@Column(name = "current_analytics_solution", length = 255)
	String currentAnalyticsSolution;

	@Column(name = "tracking_data")
	Boolean trackingData;

	@Column(name = "e_shop_hosted_on", length = 255)
	String eShopHosting;

	@Column(name = "no_of_employees", length = 32)
	String noOfEmployees;

	@Column(name = "no_of_products", length = 32)
	String noOfProducts;

	@Column(name = "tracking_data_type", length = 255)
	String trackingDataTypes;

	@Column(name = "consumers_from", length = 255)
	String consumersFrom;

	@Column(name = "expectation_comment", length = 255)
	String expectationComment;

	@OneToOne
	@JsonIgnore
	@JoinColumn(name = "accountid", referencedColumnName = "accountid")
	Account account;

	@Column(name = "city", length = 255)
	String city;

	@Column(name = "state", length = 255)
	String state;

	@Column(name = "country", length = 255)
	String country;

	public void setRegistrationInfo(RegistrationRequest request) {
		this.setName(request.getName());
		this.setPhoneNumber(request.getPhone());
		this.setAddress(request.getAddress());
		this.setCity(request.getCity());
		this.setState(request.getState());
		this.setCountry(request.getCountry());
		this.setEShopHosting(request.getEshopHosted());
		this.setHeadquarterLocation(request.getHeadQuarted());
		this.setConsumersFrom(request.getConsumers());
		this.setNoOfEmployees(request.getEmployee());
		this.setHighestProductPrice(request.getProductPrice());
		this.setNoOfProducts(request.getProduct());
		this.setCurrentAnalyticsSolution(request.getAnalytic());
		this.setTrackingData(request.getIsUserDataTrack());
		this.setTrackingDataTypes(request.getTrackerDataType());
		this.setExpectationComment(request.getExpectationComments());
		this.setUrl(request.getUrl());
	}
}