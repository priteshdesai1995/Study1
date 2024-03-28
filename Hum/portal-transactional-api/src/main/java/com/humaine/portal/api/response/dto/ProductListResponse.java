package com.humaine.portal.api.response.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ProductListResponse {

	List<ProductIntelligence> products;
	
	Long totalCount;
	
	Integer filteredProductCount;
}
