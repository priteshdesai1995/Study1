package com.humaine.collection.api.rest.service;

import java.time.OffsetDateTime;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.request.dto.UserEventRequest;

public interface UserSessionService {

	UserSession startUsersession(UserEventRequest userEventRequest, OffsetDateTime timestemp) throws APIException;

	UserSession endUsersession(UserEventRequest userEventRequest, OffsetDateTime timestemp) throws APIException;

	boolean checkSessionExist(String session);

	void validateSession(String session) throws APIException;

	UserSession validateUserSessionValidation(String session, UserEvents event);

	void checkRequestedAccountWithAPIKey(Long requestedAccountId);

	UserEvent saveEndEvent(EnddedSession endSession);
}
