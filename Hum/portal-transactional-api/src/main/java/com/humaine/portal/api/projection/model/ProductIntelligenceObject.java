package com.humaine.portal.api.projection.model;

import java.util.List;

public interface ProductIntelligenceObject {

	String getProductId();

	String getName();

	String getProductImage();

	Long getTotalCount();
	
	List<String> getUserIds();

	Long getSaleAmount();

	Double getTotalQty();
	
	Long getTotalUserCount();
}
