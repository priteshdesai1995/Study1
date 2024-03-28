package com.humaine.transactional.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Table(name = "usersessionarchive", indexes = {
		@Index(name = "index_usersessionarchive", columnList = "sessionid,userid,accountid") })
@NoArgsConstructor
@Getter
@Setter
public class UserSessionArchive {

	@Column(name = "sessionid")
	String session;

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

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;
}
