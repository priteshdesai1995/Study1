package com.humaine.portal.api.response.dto;

import java.util.HashMap;
import java.util.Map;

import com.humaine.portal.api.projection.model.ProductIntelligenceObject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductIntelligenceListResponse {

	private String productId;
	
	private String name;
	
	private String productImage;
	
	Map<String, Double> count = new HashMap<String, Double>();
	
	public ProductIntelligenceListResponse(ProductIntelligenceObject obj) {
		this.productId = obj.getProductId();
		this.name = obj.getName();
		this.productImage = obj.getProductImage();
	}
}
