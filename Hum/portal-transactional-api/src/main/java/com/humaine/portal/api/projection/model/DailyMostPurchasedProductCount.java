package com.humaine.portal.api.projection.model;

public interface DailyMostPurchasedProductCount {

	String getProductName();

	String getProductPageURL();

	Long getViewCount();

	Long getPurchasedcount();
	
	String getImage();
}
