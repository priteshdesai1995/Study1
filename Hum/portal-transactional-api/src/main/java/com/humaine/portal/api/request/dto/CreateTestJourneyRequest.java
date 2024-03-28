package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
public class CreateTestJourneyRequest {

	@NotNull(message = "{api.error.user-group.ids.null}{error.code.separator}{api.error.user-group.ids.null.code}")
	Long groupId;
	
	@NotBlank(message = "{api.error.test-journey.first-interest.null}{error.code.separator}{api.error.test-journey.first-interest.null.code}")
	String firstInterest;
	
	String decison;
	
	String purchaseAddCart;
	
	String purchaseBuy;
	
	String purchaseOwnership;
}
