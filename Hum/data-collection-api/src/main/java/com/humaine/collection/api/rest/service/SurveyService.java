package com.humaine.collection.api.rest.service;

import com.humaine.collection.api.model.SurveyEventRequest;
import com.humaine.collection.api.model.Account;

public interface SurveyService {

	Long startSurvey(SurveyEventRequest surveyEventRequest, Account acc);

	Long endSurvey(String userId, String sessionId);

	Boolean checkStatusAPI(String surveyUuid);
	
	Account validateRequest(String userId);
}
