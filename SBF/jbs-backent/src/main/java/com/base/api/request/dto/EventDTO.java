package com.base.api.request.dto;


import java.util.List;

import com.base.api.entities.Event;
import com.base.api.entities.EventParticipants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

	private Event event;
	private List<EventParticipants> participants;
	private String emails;
	
}
