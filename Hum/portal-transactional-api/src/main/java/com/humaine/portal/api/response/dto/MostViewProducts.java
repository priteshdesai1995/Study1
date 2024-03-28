package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.projection.model.DailyMostViewedProductCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostViewProducts {

	Long clickCount;

	String productName;

	String productPageURL;

	Long purchaseCount;

	String image;
	
	public MostViewProducts(DailyMostViewedProductCount productData) {
		if (productData == null)
			return;
		this.clickCount = productData.getViewCount();
		this.productName = productData.getProductName();
		this.productPageURL = productData.getProductPageURL();
		this.purchaseCount = productData.getProductPurchaseCount();
		this.image = productData.getImage();
	}
}
