package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.projection.model.DailyMostViewedCategoryCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MostViewCategory {

	String category;

	Long clickCount;

	Long categoryViewCount;

	String pageUrl;

	public MostViewCategory(DailyMostViewedCategoryCount catData) {
		if (catData == null)
			return;
		this.category = catData.getCategory();
		this.clickCount = catData.getViewcount();
		this.categoryViewCount = catData.getCatvisitcount();
	}
}
