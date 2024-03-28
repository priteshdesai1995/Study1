package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.base.api.enums.Recurrence;
import com.base.api.request.dto.EventDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "event_table")
@AttributeOverride(name = "id", column = @Column(name = "event_id"))
public class Event extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "recurrence")
	private String recurrence;
	
	@Column(name = "recurrence_upto")
	private String recurrenceUpto;

	@Column(name = "address")
	private String address;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "send_notifications")
	private String sendNotifications;

	@Column(name = "access_token")
	private String accessToken;
	
	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@OneToOne
	private User createdBy;
	
	@JsonProperty("google_calendar_link")
	private String googleCalendarLink = "https://www.google.com/calendar/render?action=TEMPLATE&text=dfsgsgsg&details=dfjdshf&dates=20220920T003500Z%2F20220930T003500Z";
	
	@JsonProperty("google_event_link")
	@Column(name = "google_meet_link")
	private String googleMeetLink = "https://meet.google.com/sfe-pdnn-ygz";

}
