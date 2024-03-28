package com.humaine.collection.api.request.dto;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.humaine.collection.api.annotation.FieldValueExists;
import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.rest.repository.AccountRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventRequest {

	@FieldValueExists(repository = AccountRepository.class, requiredMsg = "api.error.accountID.null", requiredMsgErorCode = "api.error.accountID.null.code", message = "api.error.account.not.found", messageCode = "api.error.account.not.found.code")
	private Long accountID;

	@NotBlank(message = "{api.error.usereventrequest.userID.null}{error.code.separator}{api.error.usereventrequest.userID.null.code}")
	private String userID;

	@NotBlank(message = "{api.error.usereventrequest.sessionID.null}{error.code.separator}{api.error.usereventrequest.sessionID.null.code}")
	private String sessionID;

	private String deviceId;

	private String deviceType;

	@NotNull(message = "{api.error.eventID.null}{error.code.separator}{api.error.eventID.null.code}")
	private UserEvents eventID;

	private String productID;

	private String pageURL;

	private Long pageLoadTime;

	@Positive(message = "{api.error.usereventrequest.saleAmount.invalid}{error.code.separator}{api.error.usereventrequest.saleAmount.invalid.code}")
	private Float saleAmount;

	@Positive(message = "{api.error.usereventrequest.productQuantity.invalid}{error.code.separator}{api.error.usereventrequest.productQuantity.invalid.code}")
	private Long productQuantity;

	String city;

	String state;

	String country;

	Double latitude;

	Double longitude;

	String companyName;

	String postTitle;
	
	String postId;
	
	String menuId;
	
	String menuURL;
	
	String menuName;
	
	String socialMediaURL;
	
	String socialMediaPlateForm;
		
	String targetElement;
	
	String highlightedText;
	
	String productImageURL;
	
	Double ratingValue;
	
	String selectedElement;
	
	String couponCode;
	
	private Map<String, Object> productMetaData;

	public UserEventRequest(Long accountID, String userID, String sessionID, String deviceId, String deviceType,
			UserEvents eventID, String productID, String pageURL, Long pageLoadTime, Float saleAmount,
			Long productQuantity, String city, String state, String country, Double latitude, Double longitude,
			String companyName) {
		super();
		this.accountID = accountID;
		this.userID = userID;
		this.sessionID = sessionID;
		this.deviceId = deviceId;
		this.deviceType = deviceType;
		this.eventID = eventID;
		this.productID = productID;
		this.pageURL = pageURL;
		this.pageLoadTime = pageLoadTime;
		this.saleAmount = saleAmount;
		this.productQuantity = productQuantity;
		this.city = city;
		this.state = state;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
		this.companyName = companyName;
	}
}
