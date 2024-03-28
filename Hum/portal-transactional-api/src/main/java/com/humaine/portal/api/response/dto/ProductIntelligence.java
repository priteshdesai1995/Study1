package com.humaine.portal.api.response.dto;

import java.util.Map;

import com.humaine.portal.api.projection.model.ProductIntelligenceQueryProjection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductIntelligence {

	Long dailyProductIntelligenceId;

	Map<String, Double> count;

	String name;

	String productId;

	String productImage;

	public ProductIntelligence(ProductIntelligenceQueryProjection productIntelligence) {
		this.dailyProductIntelligenceId = productIntelligence.getDailyProductIntelligenceId();
		this.name = productIntelligence.getProductName();
		this.productId = productIntelligence.getProductId();
		this.productImage = productIntelligence.getProductImage();
	}
	
	
	
}
