package com.humaine.collection.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.request.dto.UserEventRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usersession")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSession {

	@Id
	@Column(name = "sessionid")
	String id;

	@Column(name = "userid")
	String user;

	@Column(name = "accountid")
	Long account;

	@Column(name = "deviceid")
	String deviceId;

	@Column(name = "city")
	String city;

	@Column(name = "state")
	String state;

	@Column(name = "country")
	String country;

	@Column(name = "lat")
	Double latitude;

	@Column(name = "long")
	Double longitude;

	@Column(name = "starttime")
	OffsetDateTime startTime;

	@Column(name = "endtime")
	OffsetDateTime endTime;

	public UserSession(UserEventRequest userEventRequest, OffsetDateTime timestemp) {
		super();
		this.id = userEventRequest.getSessionID();
		this.user = userEventRequest.getUserID();
		this.account = userEventRequest.getAccountID();
		this.deviceId = userEventRequest.getDeviceId();
		if (UserEvents.START.equals(userEventRequest.getEventID())) {
			this.city = userEventRequest.getCity();
			this.state = userEventRequest.getState();
			this.country = userEventRequest.getCountry();
			this.latitude = userEventRequest.getLatitude();
			this.longitude = userEventRequest.getLongitude();
		}
		this.startTime = timestemp;
	}

	@Override
	public String toString() {
		return "UserSession [id=" + id + ", user=" + user + ", account=" + account + ", deviceId=" + deviceId
				+ ", city=" + city + ", state=" + state + ", country=" + country + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
