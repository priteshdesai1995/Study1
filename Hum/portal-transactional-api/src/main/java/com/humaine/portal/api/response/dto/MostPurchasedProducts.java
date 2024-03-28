package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.projection.model.DailyMostPurchasedProductCount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostPurchasedProducts {

	String productName;

	String productPageURL;

	Long clickCount;

	Long purchasedCount;
	
	String image;

	public MostPurchasedProducts(DailyMostPurchasedProductCount productData) {
		if (productData == null)
			return;
		this.productName = productData.getProductName();
		this.productPageURL = productData.getProductPageURL();
		this.clickCount = productData.getViewCount();
		this.purchasedCount = productData.getPurchasedcount();
		this.image = productData.getImage();

	}
}
