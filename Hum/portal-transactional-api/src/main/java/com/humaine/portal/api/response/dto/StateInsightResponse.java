package com.humaine.portal.api.response.dto;

import java.util.List;

import com.humaine.portal.api.projection.model.MonthlyProductData;
import com.humaine.portal.api.projection.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * This response DTO contains all the objects or properties that is needed in the state Insights page 
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class StateInsightResponse {

	List<Product> popularProducts;
	
	List<Product> leastPopularProducts;
	
	private Float monthlyPercentage;
	
	private Float todayPercentage;
	
	MonthlyProductData monthlyProductDataByState; 
}
