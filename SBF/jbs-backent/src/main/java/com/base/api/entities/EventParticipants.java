package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "event_participants")
@AttributeOverride(name = "id", column = @Column(name = "event_participant_id"))
public class EventParticipants extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@OneToOne
	private Event event;

	@OneToOne
	private User user;

}