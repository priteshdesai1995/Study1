package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.VERSION_V1;
import static com.base.api.constants.PermissionConstants.CREATE_EVENT;
import static com.base.api.constants.PermissionConstants.DELETE_EVENT_BY_ID;
import static com.base.api.constants.PermissionConstants.GET_ADMIN_LIST;
import static com.base.api.constants.PermissionConstants.GET_ALL_EVENTS;
import static com.base.api.constants.PermissionConstants.GET_EVENT_BY_ID;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_EVENT;
import static com.base.api.constants.PermissionConstants.UPDATE_PLAN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.base.api.constants.PermissionConstants.GET_ACTIVE_USERS;
import static com.base.api.constants.PermissionConstants.GET_ADMIN_LIST;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.dto.filter.EventFilter;
import com.base.api.entities.Event;
import com.base.api.entities.User;
import com.base.api.enums.UserStatus;
import com.base.api.exception.APIException;
import com.base.api.repository.EventRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.EventDTO;
import com.base.api.service.EventService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@SuppressWarnings("unused")
@RestController
@Slf4j
@RequestMapping("/event")
public class Eventcontroller {

	@Autowired
	EventService eventService;

	@Autowired
	EventRepository eventRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ResourceBundle resourceBundle;
	
	@PostMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to Get all the EVENTS", notes = PERMISSION + GET_ALL_EVENTS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_EVENTS })
	public ResponseEntity<TransactionInfo> getAllEvents(@RequestBody EventFilter eventFilter) throws Exception {
		
		log.info("EventController : getAllEvents ");
		
		List<Event> result = eventService.getAllEvents(eventFilter);
		
		if(result == null) {
			return ResponseBuilder.buildRecordNotFoundResponse(result,resourceBundle.getString("event_list_fail"));	
		}
		return ResponseBuilder.buildOkResponse(result);
	}

	/**
	 * @param eventDTO
	 * @return
	 * @throws Exception
	 * 
	 *                   This rest API is to create event
	 */
	
	@PostMapping(value = "/create")
	@ApiOperation(value = "API Endpoint to create the event", notes = PERMISSION + CREATE_EVENT, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_EVENT })
	public ResponseEntity<TransactionInfo> createEvent(@RequestBody Map<String, String> event) throws Exception {
		
		log.info(" EventController : createEvent ");
		
		String result = eventService.createEvent(event);
		
		if (result !=null) {
			return ResponseBuilder.buildCRUDResponse(result,resourceBundle.getString("event_create_success"),HttpStatus.CREATED);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result,
				resourceBundle.getString("event_create_fail"));
	}
	
	@GetMapping(value = "/subadmin/dropdown-list")
	@ApiOperation(value = "API Endpoint to get the admin list", notes = PERMISSION + GET_ADMIN_LIST, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ADMIN_LIST })
	public ResponseEntity<TransactionInfo> getAdminList() {
		
        log.info("Api hit for get the active status");
		
        List<User> user = eventService.findByStatus("UserStatus.ACTIVE");
		if (user == null) {
			log.error("active status record is not available:");
		}
		log.info("record is available :");
		
		return ResponseBuilder.buildOkResponse(user);
	}
	
	/**
	 * @param eventId
	 * @return
	 * @throws Exception This rest API is to get the event
	 */
	@PostMapping(value = "/show")
	@ApiOperation(value = "API Endpoint to Get the event", notes = PERMISSION + GET_EVENT_BY_ID, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_EVENT_BY_ID })
	public ResponseEntity<TransactionInfo> getEvent(@RequestParam(("event_id")) UUID eventId) throws Exception {

		log.info("API hit of the get event");
		
		EventDTO result = eventService.getEventById(eventId);
		
		if (result == null) {
			throw new APIException("event.not.found", HttpStatus.NOT_FOUND);
		}
		
		log.info("event Listing call ends");
		return ResponseBuilder.buildOkResponse(result);
	}
	
	@PostMapping(value = "/update")
	@ApiOperation(value = "API Endpoint to Update the event", notes = PERMISSION + UPDATE_EVENT, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_EVENT })
	public ResponseEntity<TransactionInfo> updateEvent(@RequestBody Map<String, String> event) {
		
		log.info("API hit of the update event");
		
		String result = eventService.updateEvent(event);
		
		if (result.equals(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("event_updated"), HttpStatus.OK);
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("event_update_fail"));
	}
	
	@GetMapping(value = "/getActiveUserList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to create the event", notes = PERMISSION + GET_ACTIVE_USERS, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ACTIVE_USERS })
	public ResponseEntity<TransactionInfo> getActiveUsers(@RequestParam("search") String search) {

		User userEntity = userRepo.findByEmailContaining(search,"Active");
		
		if (userEntity != null) {

			return ResponseBuilder.buildWithMessage("record.found");
		}
		return ResponseBuilder.buildWithMessage("record.not.found");
	}
	
	@PostMapping(value = "/delete")
	@ApiOperation(value = "API Endpoint to Delete the event", notes = PERMISSION
			+ DELETE_EVENT_BY_ID, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_EVENT_BY_ID })
	public ResponseEntity<TransactionInfo> deleteEvent(@RequestBody Map<String, String> event) throws Exception {
		
		log.info("API hit of the delete event");
		
		String result = eventService.deleteEvent(event);
		
		if (result !=null) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("event_deleted"),
					HttpStatus.OK);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result,
				resourceBundle.getString("event_delete_fail"));
		
//		Optional<Event> eventData = eventRepo.findById(eventId);
//		if (eventData == null) {
//			log.error("no data found");
//			throw new EventNotFoundException("not.able.to.delete.event");
//		}
//		eventService.deleteEventById(eventId);
//		log.info("Delete the event successfully");
//		return ResponseBuilder.buildWithMessage("delete.the.event.successfully");
	}
	
	@GetMapping(value = "/listOfData")
	@ApiOperation(value = "API Endpoint to Get all the EVENTS", notes = PERMISSION + GET_ALL_EVENTS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_EVENTS })
	public ResponseEntity<TransactionInfo> getAllEvents() throws Exception {
		log.info("API hit of all the events");
		List<Event> listOfEvents = eventService.getAllEvents();
		return ResponseBuilder.buildOkResponse(listOfEvents);
	}
}
