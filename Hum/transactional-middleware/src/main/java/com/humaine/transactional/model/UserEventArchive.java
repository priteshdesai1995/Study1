package com.humaine.transactional.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.humaine.transactional.converter.JsonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class UserEventArchive {

	@Column(name = "usereventid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "userid")
	String user;

	@Column(name = "accountid")
	Long account;

	@Column(name = "eventid")
	String event;

	@Column(name = "sessionid")
	String session;

	@Column(name = "product")
	String productid;

	@Column(name = "pageurl")
	String pageUrl;

	@Column(name = "timestamp")
	OffsetDateTime timestamp;

	@Column(name = "social_media_platform", length = 255)
	String socialMediaPlateform;

	@Column(name = "social_media_url", length = 255)
	String socialMediaURL;

	@Column(name = "post_id", length = 36)
	String postId;

	@Column(name = "menu_id", length = 36)
	String menuId;

	@Column(name = "menu_name", length = 255)
	String menuName;

	@Column(name = "menu_url", length = 255)
	String menuURL;

	@Column(name = "post_title", length = 255)
	String postTitle;

	@Column(name = "product_metadata")
	@Type(type = "JsonType")
	private Map<String, Object> productMetaData;

	public UserEventArchive(UserEvent event) {
		this.id = event.getId();
		this.user = event.getUser();
		this.event = event.getEvent();
		this.account = event.getAccount();
		this.session = event.getSession();
		this.productid = event.getProduct();
		this.pageUrl = event.getPageUrl();
		this.timestamp = event.getTimestamp();
		this.socialMediaPlateform = event.getSocialMediaPlateform();
		this.socialMediaURL = event.getSocialMediaURL();
		this.postId = event.getPostId();
		this.menuId = event.getMenuId();
		this.menuName = event.getMenuName();
		this.menuURL = event.getMenuURL();
		this.postTitle = event.getPostTitle();
		this.productMetaData = event.getProductMetaData();
	}
}
