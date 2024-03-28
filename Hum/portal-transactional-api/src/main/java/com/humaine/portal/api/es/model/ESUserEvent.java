package com.humaine.portal.api.es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ESUserEvent {

	Long userEventId;

	String userId;

	String externalUserId;

	String deviceId;

	String bodegaId;

	String deviceType;

	Long pageLoadTime;

	String sessionId;

	String city;

	String state;

	String country;

	EsLatLong location;

	String sessionStartTime;

	String sessionEndTime;

	String browser;

	Long accountId;

	String eventId;

	String productId;

	String productName;

	String productDescription;

	String productCategory;

	Float productPrice;

	String productRelatedImages;

	Long saleId;

	Float saleAmount;

	Long productQuantity;

	String saleOn;

	String pageUrl;

	String menuId;

	String menuName;

	String menuUrl;

	String postId;

	String postTitle;

	String socialMediaPlatform;

	String socialMediaUrl;

	String timestamp;
}