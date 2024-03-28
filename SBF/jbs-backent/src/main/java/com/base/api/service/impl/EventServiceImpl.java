package com.base.api.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.base.api.dto.filter.EventFilter;
import com.base.api.dto.filter.FilterBase;
import com.base.api.entities.Event;
import com.base.api.entities.EventParticipants;
import com.base.api.entities.Template;
import com.base.api.entities.User;
import com.base.api.exception.APIException;
import com.base.api.exception.EventNotFoundException;
import com.base.api.exception.NoActiveStatusFoundException;
import com.base.api.repository.EventParticipantsRepo;
import com.base.api.repository.EventRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.EventDTO;
import com.base.api.request.dto.TemplateDTO;
import com.base.api.service.EventService;
import com.base.api.gateway.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EventParticipantsRepo eventParticipantsRepository;

	@Autowired
	EntityManager entityManager;

	/**
	 * This method is to get all the events
	 * @throws ParseException 
	 */
	
	
	@Override
	public List<Event> getAllEvents(EventFilter filter) throws ParseException  {
		log.info("EventServiceImpl::start Get-All-Events");
		
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getEvent_name() != null && !filter.getEvent_name().isEmpty()) {
			query.append(" LOWER(u.name) LIKE '%" + filter.getEvent_name().toLowerCase() + "%' ");
		}
		if (filter.getCreated_by() != null && !filter.getCreated_by().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.createdBy = '" + filter.getCreated_by().toLowerCase() + "'");
		}
		if (filter.getFrom_date() != null && !filter.getFrom_date().isEmpty() && filter.getTo_date() != null
				&& !filter.getTo_date().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date fromDate = sdf.parse(filter.getFrom_date());
			Date toDate = sdf.parse(filter.getTo_date());

			query.append(" TO_DATE(u.startDate, 'YYYY-MM-DD') = '" + fromDate
					+ "' and TO_DATE(u.endDate, 'YYYY-MM-DD') = '" + toDate + "'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQueryForSuggestion(filterBase, query.toString());
		List<Event> results = new ArrayList<Event>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Event u join u.createdBy u1 where");
		return query;
	}

	
	/**
	 * This method is used to create a event
	 * 
	 * @return
	 */
	@Transactional
	@Override
	public String createEvent(Map<String, String> event) {
		
		log.info("EventServiceImpl:: createEvent");

		Event entity = new Event();
		entity.setName(event.get("eventName"));
		entity.setDescription(event.get("eventDescription"));
		entity.setAddress(event.get("eventAddress"));
		entity.setAccessToken(event.get("access_token"));
		entity.setRecurrence(event.get("recurrence"));
		entity.setRecurrenceUpto(event.get("recurrence_upto"));
		entity.setStartDate(event.get("eventStartDateTime"));
		entity.setEndDate(event.get("eventEndDateTime"));
		entity.setEmailId(event.get("email_id"));
		entity.setSendNotifications(event.get("sendNotifications"));	
		entity.setCreatedBy(userRepository.findById(UUID.fromString(event.get("userId"))).get());
		
		eventRepository.save(entity);
		log.info("EventServiceImpl:: End create-event");
		log.info("event create succesfully");
		return HttpStatus.OK.name();
	}

	/**
	 * This is an implementation to get the event
	 */
	@Override
	public EventDTO getEventById(UUID eventId)throws Exception {
		
		log.info("EventServiceImpl:: getEventById");
		
		Event event = eventRepository.findById(eventId).get();
		
		List<EventParticipants> eventParticipantsEntities = new ArrayList<EventParticipants>();
		if (event != null) {
			eventParticipantsEntities = eventParticipantsRepository.findByEvent(event);
		}
		String emails = "";
		for (EventParticipants eventParticipantsEntity : eventParticipantsEntities) {
			emails += eventParticipantsEntity.getUser().getUserProfile().getEmail() + ",";
		}
		emails = !emails.isEmpty() ? emails.substring(0, emails.length() - 1) : emails;
		
		EventDTO eventDTO = new EventDTO();
		eventDTO.setEvent(event);
		eventDTO.setParticipants(eventParticipantsEntities);
		eventDTO.setEmails(emails);
		return eventDTO;
	}
	
	/**
	 * This method is used to update the event
	 */

	@Transactional
	@Override
	public String updateEvent(Map<String, String> event) {
		
		log.info("EventServiceImpl : updateEvent");
		Event entity = eventRepository.findById(UUID.fromString(event.get("eventId"))).get();

		if (entity == null)
			return HttpStatus.NOT_FOUND.name();

		entity.setName(event.get("eventName"));
		entity.setDescription(event.get("eventDescription"));
		entity.setAddress(event.get("eventAddress"));
		entity.setRecurrence(event.get("recurrence"));
		entity.setRecurrenceUpto(event.get("recurrence_upto"));
		entity.setAccessToken(event.get("access_token"));
		entity.setEmailId(event.get("email_id"));
		entity.setStartDate(event.get("eventStartDateTime"));
		entity.setEndDate(event.get("eventEndDateTime"));
		entity.setSendNotifications(event.get("sendNotifications"));
		entity.setCreatedBy(userRepository.findById(UUID.fromString(event.get("userId"))).get());

		eventRepository.save(entity);
		
		String attendees = event.get("add_attendee");
		
		if(!attendees.isEmpty() && !attendees.contains(",")) {
			deleteEventParticipants(entity);
			addEventParticipants(attendees,entity);
		}
		
		if(event.get("add_attendee").contains(",")) {
			deleteEventParticipants(entity);
			for(String email : event.get("add_attendee").split(",")) {
				addEventParticipants(email,entity);
			}
		}
		return HttpStatus.OK.name();
	}
	
	private void addEventParticipants(String email, Event event) {
		
		log.info("EventServiceImpl : addEventParticipants");
		User user = userRepository.findByEmail(email).get();
		
		if(user != null) {
			EventParticipants eventParticipants = new EventParticipants();
			
			eventParticipants.setEvent(event);
			eventParticipants.setUser(user);
			
			eventParticipantsRepository.save(eventParticipants);
		}
	
	}
	
	/**
	 * This method will delete the event by id.
	 */
	@Override
	public String deleteEvent(@RequestBody Map<String, String> deleteReq) {

		log.info("EventServiceImpl:: deleteEvent");
		
		Event event = eventRepository.findById(UUID.fromString(deleteReq.get("eventId"))).get();
		
		if(event != null) {
			
			deleteEventParticipants(event);
			eventRepository.delete(event);
			
			return "event deleted successfully"; 
		}
		return "event not found";
	}

	private void deleteEventParticipants(Event event) {
		
		log.info("EventServiceImpl : deleteEventParticipants");
		
		for (EventParticipants eventParticipants : eventParticipantsRepository.findByEvent(event)) {
			eventParticipantsRepository.delete(eventParticipants);
		}
	}

	@Override
	public List<Event> getAllEvents() throws Exception {

		log.info("EventServiceImpl : GetAllEvents");
		List<Event> listOfEvents = eventRepository.findAll();
		if (listOfEvents == null) {
			throw new EventNotFoundException("There is no data avilable in database:");
		}
		log.info("EventServiceImpl::end Get-All-Events");
		return listOfEvents;
	}
	@Override
	public List<User> findByStatus(String string) {
		log.info("EventServiceImpl:: start get-active-status");
		List<User> activeStatusList = userRepository.findAll();
		if (activeStatusList.isEmpty()) {
			log.error("No Active status found in the Database");
			throw new NoActiveStatusFoundException("no.active.status.found.in.db");
		}
		log.info("EventServiceImpl:: end get-active-status");
		return activeStatusList;
	}

}
