package com.base.api.service;


import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.base.api.dto.filter.EventFilter;
import com.base.api.entities.Event;
import com.base.api.entities.User;
import com.base.api.request.dto.EventDTO;


@Service
public interface EventService {

	public String createEvent(Map<String, String> event);

	//public Event getEventById(UUID eventId) throws Exception;
	 public EventDTO getEventById(UUID eventId) throws Exception;

	public String deleteEvent(@RequestBody Map<String, String> deleteReq) throws Exception;

	public List<User> findByStatus(String string);

	public List<Event> getAllEvents(EventFilter eventFilter) throws ParseException;

	public String updateEvent(Map<String, String> eventEntity);
 
	public List<Event> getAllEvents() throws Exception;

}
