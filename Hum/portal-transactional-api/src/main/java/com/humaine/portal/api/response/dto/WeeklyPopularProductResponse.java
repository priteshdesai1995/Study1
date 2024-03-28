package com.humaine.portal.api.response.dto;

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
public class WeeklyPopularProductResponse {

	String productId;
	
	String productName;
	
	String productImage;

	float productPrice;
	
	int soldTotalProducts;
	
}
