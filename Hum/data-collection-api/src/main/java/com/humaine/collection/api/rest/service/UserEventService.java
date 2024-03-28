package com.humaine.collection.api.rest.service;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.PageLoadData;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.request.dto.PageLoadEventRequest;
import com.humaine.collection.api.request.dto.UserEventRequest;

public interface UserEventService {

	UserEvent addOrEditUserEvent(UserEventRequest userEventRequest) throws APIException;

	PageLoadData savePageLoadData(PageLoadEventRequest request);
}
