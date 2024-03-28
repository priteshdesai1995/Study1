package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.projection.model.DailyEventDistributionCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDistributionResponse {

	Float productViewPercentage;

	Float blogPostEventPercentage;

	Float wishListPercentage;

	Float reviewPercentage;

	Float buyPercentage;

	Float otherPercentage;

	public EventDistributionResponse(DailyEventDistributionCount counts) {
//		if (counts == null)
//			return;
//		this.productViewPercentage = counts.getProductViewPercentage();
//		this.blogPostEventPercentage = counts.getBlogPostEventPercentage();
//		this.wishListPercentage = counts.getWishListPercentage();
//		this.reviewPercentage = counts.getReviewPercentage();
//		this.buyPercentage = counts.getBuyPercentage();
//		this.otherPercentage = counts.getOtherPercentage();
	}
}
