package com.humaine.admin.api.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.humaine.admin.api.annotation.FieldValueExists;
import com.humaine.admin.api.repository.AccountRepository;

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

	@FieldValueExists(repository = AccountRepository.class, requiredMsg = "api.error.accountID.null", requiredMsgErorCode = "api.error.accountID.null.code", message = "api.error.account.not.found", messageCode = "api.error.account.not.found.code")
	Long accountID;

	@NotBlank(message = "{api.error.fullName.null}{error.code.separator}{api.error.fullName.null.code}")
	String username;

	@NotBlank(message = "{api.error.fullName.null}{error.code.separator}{api.error.fullName.null.code}")
	String name;

	@NotBlank(message = "{api.error.address.null}{error.code.separator}{api.error.address.null.code}")
	String address;

	@NotBlank(message = "{api.error.phonenumber.null}{error.code.separator}{api.error.phonenumber.null.code}")
	String phone;

	@NotBlank(message = "{api.error.city.null}{error.code.separator}{api.error.city.null.code}")
	String city;

	@NotBlank(message = "{api.error.state.null}{error.code.separator}{api.error.state.null.code}")
	String state;

	@NotBlank(message = "{api.error.country.null}{error.code.separator}{api.error.country.null.code}")
	String country;

	
	List<String> industry;

	@NotNull(message = "{api.error.businessurl.null}{error.code.separator}{api.error.businessurl.null.code}")
	String url;

	@NotBlank(message = "{api.error.eshophosting.null}{error.code.separator}{api.error.eshophosting.null.code}")
	String eshopHosted;

	@NotBlank(message = "{api.error.headquarterlocation.null}{error.code.separator}{api.error.headquarterlocation.null.code}")
	String headQuarted;

	String consumers;

	String employee;

	String product;

	String productPrice;

	String analytic;

	Boolean isUserDataTrack;

	List<String> provider;

	String trackerDataType;

	String expectationComments;

	@NotNull(message = "{api.error.homepageurl.null}{error.code.separator}{api.error.homepageurl.null.code}")
	String homePageUrl;

	@Valid
	List<PageMasterData> detailedPage;

	String email;

	public RegistrationRequest(Account acc) {
		this.accountID = acc.getId();
		if (acc.getBusinessInformation() != null) {
			BusinessInformation businessInfo = acc.getBusinessInformation();
			this.name = businessInfo.getName();
			this.address = businessInfo.getAddress();
			this.phone = businessInfo.getPhoneNumber();
			this.city = businessInfo.getCity();
			this.state = businessInfo.getState();
			this.country = businessInfo.getCountry();
			this.url = businessInfo.getUrl();
			this.eshopHosted = businessInfo.getEShopHosting();
			this.headQuarted = businessInfo.getHeadquarterLocation();
			this.consumers = businessInfo.getConsumersFrom();
			this.employee = businessInfo.getNoOfEmployees();
			this.product = businessInfo.getNoOfProducts();
			this.productPrice = businessInfo.getHighestProductPrice();
			this.analytic = businessInfo.getCurrentAnalyticsSolution();
			this.isUserDataTrack = businessInfo.getTrackingData();
			this.trackerDataType = businessInfo.getTrackingDataTypes();
			this.expectationComments = businessInfo.getExpectationComment();
			this.username = acc.getUsername();
			this.email = acc.getEmail();
		}

		this.homePageUrl = acc.getSiteUrl();
		if (acc.getIndustries() != null) {
			this.industry = acc.getIndustries();
		}
		if (acc.getDataTrackingProviders() != null) {
			this.provider = acc.getDataTrackingProviders();
		}
		if (acc.getPages() != null) {
			this.setDetailedPage(acc.getPages().stream().map(p -> new PageMasterData(p)).collect(Collectors.toList()));
		}
	}
}
