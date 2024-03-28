package com.humaine.portal.api.projection.model;

/**
 * @author 
 *
 * This interface Product is used for most popular product and least popular product in dashboard API
 */
public interface Product {

	String getProductId();
	
	String getName();
	
	String getProductImage();
	
	Long getTotalSoldQuantities();
	
	Double getTotalSoldAmount();
}
