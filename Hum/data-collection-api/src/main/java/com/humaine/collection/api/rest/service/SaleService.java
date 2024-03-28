package com.humaine.collection.api.rest.service;

import java.time.OffsetDateTime;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Sale;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.request.dto.UserEventRequest;

public interface SaleService {
	Sale addSale(UserEventRequest userEventRequest, OffsetDateTime timestemp, UserEvent userEvent) throws APIException;
}
