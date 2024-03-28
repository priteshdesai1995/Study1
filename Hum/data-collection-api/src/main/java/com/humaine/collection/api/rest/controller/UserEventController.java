package com.humaine.collection.api.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.request.dto.PageLoadEventRequest;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.service.UserEventService;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ResponseBuilder;
import com.humaine.collection.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Event", description = "Capture Event Data")
@RequestMapping("userEvent")
public class UserEventController {

	@Autowired
	private UserEventService userEventService;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	UserSessionService userSessionService;

	@ApiOperation(value = "User Event Object that needs to be captured from from E-comm site", notes = "User Event Object that needs to be captured from from E-comm site", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@PostMapping(path = "", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> saveUserEvent(@Valid @RequestBody UserEventRequest userEventRequest)
			throws APIException {
		this.userSessionService.checkRequestedAccountWithAPIKey(userEventRequest.getAccountID());
		UserSession validation = this.userSessionService.validateUserSessionValidation(userEventRequest.getSessionID(),
				userEventRequest.getEventID());
		if (validation != null) {
			ResponseEntity<TransactionInfo> res = ResponseBuilder.buildMessageCodeResponse(
					errorMessageUtils.getMessage("api.error.session.notfound",
							new Object[] { userEventRequest.getSessionID() }),
					errorMessageUtils.getMessage("api.error.session.notfound.code"), HttpStatus.INTERNAL_SERVER_ERROR);
			return res;
		}
		this.userEventService.addOrEditUserEvent(userEventRequest);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode(
				"api.success.usereventrequest.saveUserEvent", null, "api.success.usereventrequest.saveUserEvent.code"));
	}

	@ApiOperation(value = "page Load data captured from from E-comm site", notes = "page Load data captured from from E-comm site", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@PostMapping(path = "/pageLoad", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> savePageLoadEvent(@Valid @RequestBody PageLoadEventRequest request)
			throws APIException {
		this.userSessionService.checkRequestedAccountWithAPIKey(request.getAccountID());
		UserSession validation = this.userSessionService.validateUserSessionValidation(request.getSessionID(), null);
		if (validation != null) {
			ResponseEntity<TransactionInfo> res = ResponseBuilder.buildMessageCodeResponse(
					errorMessageUtils.getMessage("api.error.session.notfound", new Object[] { request.getSessionID() }),
					errorMessageUtils.getMessage("api.error.session.notfound.code"), HttpStatus.INTERNAL_SERVER_ERROR);
			return res;
		}
		this.userEventService.savePageLoadData(request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode(
				"api.success.usereventrequest.saveUserEvent", null, "api.success.usereventrequest.saveUserEvent.code"));
	}
}
