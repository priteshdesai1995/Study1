package com.humaine.collection.api.rest.service;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.request.dto.UserEventRequest;

public interface UserService {
	User addOrEditUser(UserEventRequest userEventRequest) throws APIException;
}
