package com.humaine.collection.api.es.model;

import org.apache.commons.lang3.StringUtils;

import com.humaine.collection.api.es.projection.model.ElasticUserEvent;
import com.humaine.collection.api.util.BrowserUtils;
import com.humaine.collection.api.util.DateUtils;

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

	public ESUserEvent(ElasticUserEvent event, String browser) {

		this.userEventId = event.getUsereventid();
		this.accountId = event.getAccountid();
		this.eventId = event.getEventid();
		this.pageUrl = event.getPageurl();
		this.menuId = event.getMenuid();
		this.menuName = event.getMenu_name();
		this.menuUrl = event.getMenu_url();
		this.postId = event.getPost_id();
		this.postTitle = event.getPost_title();
		this.socialMediaPlatform = event.getSocial_media_platform();
		this.socialMediaUrl = event.getSocial_media_url();
		this.timestamp = DateUtils.parse(event.getTimestamp());

		if (!StringUtils.isBlank(event.getSessionid())) {
			EsLatLong latLong = null;
			if (event.getLat() != null && event.getLon() != null) {
				latLong = new EsLatLong(event.getLat(), event.getLon());
			}
			this.sessionId = event.getSessionid();
			this.city = event.getCity();
			this.state = event.getState();
			this.country = event.getCountry();
			this.sessionStartTime = DateUtils.parse(event.getStarttime());
			this.sessionEndTime = DateUtils.parse(event.getEndtime());
			this.browser = BrowserUtils.getBrowser(browser);
			this.location = latLong;
		}

		if (event.getSaleid() != null) {
			this.saleId = event.getSaleid();
			this.saleAmount = event.getSaleamount();
			this.productQuantity = event.getProductquantity();
			this.saleOn = DateUtils.parse(event.getSaleon());
		}

		if (!StringUtils.isBlank(event.getUserid())) {
			this.userId = event.getUserid();
			this.externalUserId = event.getExternaluserid();
			this.deviceId = event.getDeviceid();
			this.bodegaId = event.getBodegaid();
			this.deviceType = event.getDevicetype();
			this.pageLoadTime = event.getPageloadtime();
		}

		if (!StringUtils.isBlank(event.getProductid())) {
			this.productId = event.getProductid();
			this.productName = event.getName();
			this.productDescription = event.getDescription();
			this.productCategory = event.getCategory();
			this.productPrice = event.getPrice();
			this.productRelatedImages = event.getRelatedimages();
		}

	}
}