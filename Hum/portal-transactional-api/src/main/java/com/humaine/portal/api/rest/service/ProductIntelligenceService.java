package com.humaine.portal.api.rest.service;

import org.springframework.data.domain.Pageable;

import com.humaine.portal.api.request.dto.ProductIntelligenceListRequest;
import com.humaine.portal.api.response.dto.ProductListResponse;

public interface ProductIntelligenceService {

	public ProductListResponse getProductIntelligenceList(long accountId, Pageable paging,
			ProductIntelligenceListRequest request);

}
