package com.humaine.portal.api.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.projection.model.ProductIntelligenceQueryProjection;
import com.humaine.portal.api.request.dto.ProductIntelligenceListRequest;
import com.humaine.portal.api.response.dto.ProductIntelligence;
import com.humaine.portal.api.response.dto.ProductListResponse;
import com.humaine.portal.api.rest.repository.DailyProductIntelligenceRepository;
import com.humaine.portal.api.rest.service.ProductIntelligenceService;
import com.humaine.portal.api.util.ErrorMessageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductIntelligenceServiceImpl implements ProductIntelligenceService {

	@Value("${product.default.title}")
	private String defaultProductTitle;

	@Autowired
	private DailyProductIntelligenceRepository dailyProductIntelligenceRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	/**
	 *
	 * This is impl method that will give the productIntelligence 
	 * and in repository it sort autometically using Pageable 
	 *
	 */
	@Override
	public ProductListResponse getProductIntelligenceList(long accountId, Pageable paging,
			ProductIntelligenceListRequest request) {

		log.info("getProductIntelligenceList : service impl");

		long totalCount = dailyProductIntelligenceRepository.
				getDailyProductIntelligenceCountForAccountId(accountId);
		List<ProductIntelligenceQueryProjection> productList = dailyProductIntelligenceRepository
				.getDailyProductIntelligenceByAccountId(accountId, paging);
		ProductListResponse result = new ProductListResponse();
		List<ProductIntelligence> productIntelligenceList = new ArrayList<>();
				
		result.setTotalCount(totalCount);
		
		if (!productList.isEmpty()) {
			productList.forEach(e -> {

				String metadataString = e.getProductMetadata();
				
				metadataString = metadataString.substring(1, metadataString.length() - 1);
				
				ProductIntelligence productIntelligence = new ProductIntelligence(e);				
				if (metadataString.isBlank() || metadataString.isEmpty()) {
					productIntelligence.setCount(null);
				} else {
					Map<String, Double> metadataMap = convertStringToMap(metadataString);
					productIntelligence.setCount(metadataMap);
				}
				
				productIntelligenceList.add(productIntelligence);

			});
			
			if (!StringUtils.isBlank(request.getFilter())) {
				// if the filter is not blank the we will get all 35 products so 
				// need to paginate manually 
				
				log.info("filter is available => "+ request.getFilter());
				
				List<ProductIntelligence> filterResult = productIntelligenceList.stream().filter(e -> {
										
					if (e.getCount() != null && e.getCount().containsKey(request.getFilter()) && e.getCount().get(request.getFilter()) > 0) {
						return true;
					}
					return false;
				}).collect(Collectors.toList());
				
				result.setTotalCount(Long.valueOf(filterResult.size()));
				
				int from = request.getPage() * request.getSize();
				int to = from + request.getSize();

				if (from > filterResult.size()) {
					filterResult = new ArrayList<>();
				} else {
					if (from <= filterResult.size() && to > filterResult.size()) {
						to = filterResult.size();
					}
					filterResult = filterResult.subList(from, to);
				}
				
				result.setFilteredProductCount(filterResult.size());
				result.setProducts(filterResult);
			}else {
				log.info("filter is not available");
				result.setFilteredProductCount(productIntelligenceList.size());
				result.setProducts(productIntelligenceList);
			}

		} else {
			log.error("product intelligence list is empty query return null list");
			throw new APIException(
					this.errorMessageUtils.getMessageWithCode("api.error.product.intelligence.list.not.available",
							new Object[] {}, "api.error.product.intelligence.list.not.available.code"));
		}
		
		return result;
	}

	/**
	 * @param metadataString
	 * @return
	 * 
	 *         This is the method to convert string into map
	 */
	private Map<String, Double> convertStringToMap(String metadataString) {
		Map<String, Double> productMetadataMap = new HashMap<>();
		String[] keyValuePair = metadataString.split(",");

		for (String pair : keyValuePair) {
			String[] entry = pair.split(":");
			String key = entry[0].replaceAll("\"", "");
			productMetadataMap.put(key.trim(), Double.parseDouble(entry[1].trim()));
		}
		return productMetadataMap;
	}

}
