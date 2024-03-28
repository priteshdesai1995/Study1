package com.humaine.portal.api.rest.olap.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.humaine.portal.api.response.dto.JournyResponse;

public interface OLAPManualJourneyService {

	void fillSuccessMatchForList(List<JournyResponse> groups, HttpServletRequest httpRequest);

	void fillSuccessMatch(JournyResponse journyResponse, HttpServletRequest httpRequest);

}
