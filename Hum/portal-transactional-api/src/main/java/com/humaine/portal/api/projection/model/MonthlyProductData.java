package com.humaine.portal.api.projection.model;

public interface MonthlyProductData {

	Long getTotalCustomers();
	
	Double getTotalPurchases();
	
	Long getTotalSoldQuantities();
	
	Double getSaleRevenue();
}
