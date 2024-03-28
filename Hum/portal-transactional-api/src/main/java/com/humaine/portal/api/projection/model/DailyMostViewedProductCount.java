package com.humaine.portal.api.projection.model;

public interface DailyMostViewedProductCount {

	Long getViewCount();

	String getProductName();

	String getProductPageURL();

	Long getProductPurchaseCount();
	
	String getImage();
}
