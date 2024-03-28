package com.humaine.portal.api.response.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.humaine.portal.api.projection.model.DailyEventDistributionCount;
import com.humaine.portal.api.projection.model.DailyMostPurchasedProductCount;
import com.humaine.portal.api.projection.model.DailyMostViewedCategoryCount;
import com.humaine.portal.api.projection.model.DailyMostViewedProductCount;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyProductsDataResponse {

	List<MostViewProducts> mostViewProducts;

	List<MostPurchasedProducts> mostPurchasedProducts;

	List<MostViewCategory> mostViewCategory;

	List<DailyEventDistributionCount> eventCounts;

	public DailyProductsDataResponse(List<DailyMostViewedProductCount> productViewData,
			List<DailyMostPurchasedProductCount> mostPurchasedProducts,
			List<DailyMostViewedCategoryCount> mostViewCategory, List<DailyEventDistributionCount> eventCount) {
		if (productViewData != null) {
			this.mostViewProducts = productViewData.stream().map(e -> new MostViewProducts(e))
					.collect(Collectors.toList());
		}

		if (mostPurchasedProducts != null) {
			this.mostPurchasedProducts = mostPurchasedProducts.stream().map(e -> new MostPurchasedProducts(e))
					.collect(Collectors.toList());
		}

		if (mostViewCategory != null) {
			this.mostViewCategory = mostViewCategory.stream().map(e -> new MostViewCategory(e))
					.collect(Collectors.toList());
		}

		if (eventCount != null) {
			this.eventCounts = eventCount;
		}
	}
}
