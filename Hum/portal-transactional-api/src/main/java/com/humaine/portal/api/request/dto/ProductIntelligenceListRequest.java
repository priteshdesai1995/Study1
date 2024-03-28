package com.humaine.portal.api.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductIntelligenceListRequest {

	Integer page;

	Integer size;

	SortBy sort;
	
	String filter;
}
