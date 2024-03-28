package com.humaine.portal.api.rest.olap.service;

import java.util.List;

import com.humaine.portal.api.response.dto.MyJourneyAnalysisResponse;

public interface OLAPMyJourneyService {

	void fillGroupNameAndBigFiveForList(List<MyJourneyAnalysisResponse> journeyList);

	void fillGroupNameAndBigFive(MyJourneyAnalysisResponse response);
}
