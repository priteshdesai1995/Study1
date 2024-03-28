package com.humaine.transactional.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.humaine.transactional.converter.JsonType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "userevent")
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
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
}
