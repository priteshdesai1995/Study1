package com.humaine.collection.api.request.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@ToString
public class ProductRequest {

	@JsonProperty("product_id")
	String productId;
	
	@JsonProperty("name")
	String productName;
	
	@JsonProperty("description")
	String productDescription;
	
	@JsonProperty("product_sku")
	String productSKU;
	
	@JsonProperty("product_image")
	String productImage;
	
	@JsonProperty("price")
	String price;
	
	@JsonProperty("specialPrice")
	String specialPrice;
	
	@JsonProperty("parent_product_id")
	String parentProductId;
	
	@JsonProperty("attributes")
	List<ProductAttributeRequest> attributes;
	
}
