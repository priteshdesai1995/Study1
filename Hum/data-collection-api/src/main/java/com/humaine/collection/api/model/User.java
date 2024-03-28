package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.util.DateUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "humainedev")
@NoArgsConstructor
@Getter
@Setter
public class User {

	@Id
	@Column(name = "userid")
	String id;

	@Column(name = "accountid")
	Long account;

	@Column(name = "externaluserid")
	String externalUser;

	@Column(name = "deviceid")
	String device;

	@Column(name = "bodegaid")
	String bodegaId;

	@Column(name = "devicetype")
	String deviceType;

	@Column(name = "pageloadtime")
	Long pageLoadTime;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	public User(String id, Long account, String device, String deviceType, Long pageLoadTime) {
		super();
		this.id = id;
		this.account = account;
		this.device = device;
		this.deviceType = deviceType;
		this.pageLoadTime = pageLoadTime;
	}

	public User(UserEventRequest userEventRequest) {
		super();
		this.id = userEventRequest.getUserID();
		this.account = userEventRequest.getAccountID();
		this.device = userEventRequest.getDeviceId();
		this.deviceType = userEventRequest.getDeviceType();
		this.pageLoadTime = userEventRequest.getPageLoadTime();
		this.createdOn = DateUtils.getCurrentTimestemp();
	}
}
