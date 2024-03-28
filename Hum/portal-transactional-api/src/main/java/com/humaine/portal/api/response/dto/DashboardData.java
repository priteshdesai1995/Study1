package com.humaine.portal.api.response.dto;

import java.util.List;

import com.humaine.portal.api.projection.model.MonthlyProductData;
import com.humaine.portal.api.projection.model.Product;
import com.humaine.portal.api.projection.model.StateWiseSoldData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardData {
	
	List<Product> popularProducts;
	
	List<Product> leastPopularProducts;
	
	List<StateWiseSoldData> stateData;
	
	List<StateWiseSoldData> leastStateData;
	
	MonthlyProductData monthlyProductData; 
}
