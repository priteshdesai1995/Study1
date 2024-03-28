package com.humaine.collection.api.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.humaine.collection.api.converter.JsonType;
import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.request.dto.UserEventRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userevent")
@NoArgsConstructor
@Getter
@Setter
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class UserEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usereventid", unique = true, nullable = false, columnDefinition = "bigserial")
	Long id;

	@Column(name = "userid")
	String user;

	@Column(name = "accountid")
	Long account;

	@Column(name = "eventid")
	String event;

	@Column(name = "sessionid")
	String session;

	@Column(name = "productid")
	String product;

	@Column(name = "pageurl")
	String pageUrl;

	@Column(name = "timestamp")
	OffsetDateTime timestamp;

	@Column(name = "social_media_platform")
	String socialMediaPlateform;

	@Column(name = "social_media_url")
	String socialMediaURL;

	@Column(name = "post_id")
	String postId;

	@Column(name = "menu_id")
	String menuId;

	@Column(name = "menu_name")
	String menuName;

	@Column(name = "menu_url")
	String menuURL;

	@Column(name = "post_title")
	String postTitle;

	@Column(name = "product_metadata")
	@Type(type = "JsonType")
	private Map<String, Object> productMetaData;

	@Column(name = "target_element")
	String targetElement;
	
	@Column(name = "highlighted_text")
	String highlightedText;
	
	@Column(name = "product_image_url")
	String productImageURL;
	
	@Column(name = "rating_value")
	Double ratingValue;
	
	@Column(name = "attribute_data")
	String selectedElement;

	public UserEvent(UserEventRequest userEventRequest, OffsetDateTime timestemp) {
		this.user = userEventRequest.getUserID();
		this.account = userEventRequest.getAccountID();
		this.event = userEventRequest.getEventID().value();
		this.session = userEventRequest.getSessionID();
		this.product = userEventRequest.getProductID();
		if (userEventRequest.getProductMetaData() != null) {
			this.productMetaData = userEventRequest.getProductMetaData();
		}
		this.socialMediaPlateform = userEventRequest.getSocialMediaPlateForm();
		this.socialMediaURL = userEventRequest.getSocialMediaURL();
		this.postId = userEventRequest.getPostId();
		this.postTitle = userEventRequest.getPostTitle();
		this.pageUrl = userEventRequest.getPageURL();
		this.timestamp = timestemp;
		this.highlightedText = userEventRequest.getHighlightedText();
		this.productImageURL = userEventRequest.getProductImageURL();
		this.ratingValue = userEventRequest.getRatingValue();
		this.selectedElement = userEventRequest.getSelectedElement();
		if (UserEvents.MENU.equals(userEventRequest.getEventID())) {
			this.menuId = userEventRequest.getMenuId();
			this.menuURL = userEventRequest.getMenuURL();
			this.menuName = userEventRequest.getMenuName();
		}

		if (!StringUtils.isBlank(userEventRequest.getTargetElement())) {
			this.targetElement = userEventRequest.getTargetElement();
		}
	}

	public UserEvent(Long id, String user, Long account, String event, String session, String product, String pageUrl,
			OffsetDateTime timestamp) {
		super();
		this.id = id;
		this.user = user;
		this.account = account;
		this.event = event;
		this.session = session;
		this.product = product;
		this.pageUrl = pageUrl;
		this.timestamp = timestamp;
	}

	public UserEvent(EnddedSession session) {
		this.user = session.getUserID();
		this.account = session.getAccountID();
		this.session = session.getSessionID();
		this.event = UserEvents.END.value();
	}
}