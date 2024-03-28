package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.projection.model.DailyProductAvg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UXInsightsStatasticsCountResponse {

	Long totalUsers;

	Long dailyAvgSessions;

	Long dailyPurchaseProduct;

	Double dailyAvgSale;

	public UXInsightsStatasticsCountResponse(Long totalUsers, Long dailyAvgSessions, DailyProductAvg productAvg) {
		super();
		this.totalUsers = totalUsers;
		this.dailyAvgSessions = dailyAvgSessions;
		if (productAvg != null) {
			this.dailyPurchaseProduct = productAvg.getAvgProducts();
			this.dailyAvgSale = productAvg.getAvgSale();
		}
	}
}
