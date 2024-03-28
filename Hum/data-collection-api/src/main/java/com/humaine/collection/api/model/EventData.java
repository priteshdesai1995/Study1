package com.humaine.collection.api.model;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mouse_event_recording")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventid", columnDefinition = "bigserial")
	Long id;

	@Column(name = "accountid")
	Long account;

	@Column(name = "pageurl")
	String pageUrl;

	@Column(name = "createdon")
	OffsetDateTime createdOn;

	@Column(name = "modifiedon")
	OffsetDateTime modifiedOn;

	@Column(name = "cursorx")
	String cursorX;

	@Column(name = "cursory")
	String cursorY;

	@Column(name = "userid")
	String userId;

	@Column(name = "sessionid")
	String sessionId;
	
	@Column(name = "window_size")
	@Type(type = "JsonType")
	Map<String, Object> windowSize;
}
