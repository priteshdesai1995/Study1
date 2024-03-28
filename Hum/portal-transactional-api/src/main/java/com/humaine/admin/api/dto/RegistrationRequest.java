package com.humaine.admin.api.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.humaine.portal.api.annotation.FieldValueExists;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BusinessInformation;
import com.humaine.portal.api.rest.repository.AccountAdminRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationRequest {

	@FieldValueExists(repository = AccountAdminRepository.class, requiredMsg = "api.error.accountID.null", requiredMsgErorCode = "api.error.accountID.null.code", message = "api.error.account.not.found", messageCode = "api.error.account.not.found.code")
	Long accountID;

	@NotBlank(message = "{api.error.fullName.null}{error.code.separator}{api.error.fullName.null.code}")
	String fullName;

	@NotBlank(message = "{api.error.address.null}{error.code.separator}{api.error.address.null.code}")
	String address;

	@NotBlank(message = "{api.error.phonenumber.null}{error.code.separator}{api.error.phonenumber.null.code}")
	String phoneNumber;

	@NotBlank(message = "{api.error.city.null}{error.code.separator}{api.error.city.null.code}")
	String city;

	@NotBlank(message = "{api.error.state.null}{error.code.separator}{api.error.state.null.code}")
	String state;

	@NotBlank(message = "{api.error.country.null}{error.code.separator}{api.error.country.null.code}")
	String country;

	List<String> industry;

	@NotNull(message = "{api.error.businessurl.null}{error.code.separator}{api.error.businessurl.null.code}")
	String businessURL;

	@NotBlank(message = "{api.error.eshophosting.null}{error.code.separator}{api.error.eshophosting.null.code}")
	String eShopHostedOn;

	@NotBlank(message = "{api.error.headquarterlocation.null}{error.code.separator}{api.error.headquarterlocation.null.code}")
	String headquarterLocation;

	String cosumersFrom;

	String noOfEmployees;

	String noOfProducts;

	String highProductPrice;

	String curAnalyticsTool;

	Boolean isUserDataTrack;

	List<String> trackingProviders;

	String trackerDataType;

	String expectationComments;

	@NotNull(message = "{api.error.homepageurl.null}{error.code.separator}{api.error.homepageurl.null.code}")
	String homePageUrl;

	@Valid
	List<PageMasterData> detailedPage;

	public RegistrationRequest(Account acc) {
		this.accountID = acc.getId();
		if (acc.getBusinessInformation() != null) {
			BusinessInformation businessInfo = acc.getBusinessInformation();
			this.fullName = businessInfo.getName();
			this.address = businessInfo.getAddress();
			this.phoneNumber = businessInfo.getPhoneNumber();
			this.city = businessInfo.getCity();
			this.state = businessInfo.getState();
			this.country = businessInfo.getCountry();
			this.businessURL = businessInfo.getUrl();
			this.eShopHostedOn = businessInfo.getEShopHosting();
			this.headquarterLocation = businessInfo.getHeadquarterLocation();
			this.cosumersFrom = businessInfo.getConsumersFrom();
			this.noOfEmployees = businessInfo.getNoOfEmployees();
			this.noOfProducts = businessInfo.getNoOfProducts();
			this.highProductPrice = businessInfo.getHighestProductPrice();
			this.curAnalyticsTool = businessInfo.getCurrentAnalyticsSolution();
			this.isUserDataTrack = businessInfo.getTrackingData();
			this.trackerDataType = businessInfo.getTrackingDataTypes();
			this.expectationComments = businessInfo.getExpectationComment();
		}

		this.homePageUrl = acc.getSiteUrl();
		if (acc.getIndustries() != null) {
			this.industry = acc.getIndustries();
		}
		if (acc.getDataTrackingProviders() != null) {
			this.trackingProviders = acc.getDataTrackingProviders();
		}
		if (acc.getPages() != null) {
			this.setDetailedPage(acc.getPages().stream().map(p -> new PageMasterData(p)).collect(Collectors.toList()));
		}
	}
}
