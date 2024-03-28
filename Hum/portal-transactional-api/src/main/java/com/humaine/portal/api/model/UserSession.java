package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

	@Override
	public String toString() {
		return "UserSession [id=" + id + ", user=" + user + ", account=" + account + ", deviceId=" + deviceId
				+ ", city=" + city + ", state=" + state + ", country=" + country + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
