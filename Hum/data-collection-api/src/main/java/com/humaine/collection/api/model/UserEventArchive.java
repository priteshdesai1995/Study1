package com.humaine.collection.api.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Table(name = "usereventarchive", indexes = {
		@Index(name = "index_usereventarchive", columnList = "usereventid,userid, accountid") })
@NoArgsConstructor
@Getter
@Setter
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
}
