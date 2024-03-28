package com.humaine.portal.api.rest.olap.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.ManualJourneyData;
import com.humaine.portal.api.response.dto.JournyResponse;
import com.humaine.portal.api.rest.olap.repository.OLAPManualJourneyRepository;
import com.humaine.portal.api.rest.olap.service.OLAPManualJourneyService;
import com.humaine.portal.api.rest.repository.TestJourneyRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.util.CommonUtils;
import javax.servlet.http.HttpServletRequest;

@Service
public class OLAPManualJourneyServiceImpl implements OLAPManualJourneyService {

	@Autowired
	OLAPManualJourneyRepository olapManualJourneyRepository;

	@Autowired
	TestJourneyRepository testJourneyRepository;

	@Autowired
	AuthService authService;

	@Override
	public void fillSuccessMatchForList(List<JournyResponse> groups, HttpServletRequest httpRequest) {
		if (groups == null || groups != null && groups.size() == 0)
			return;
		Account account = authService.getLoginUserAccount(true, httpRequest);
		List<ManualJourneyData> userGroupsRank = olapManualJourneyRepository.getGroupsRankByIds(account.getId(),
				groups.stream().map(e -> e.getJourney().getId()).collect(Collectors.toList()));
		Map<Long, ManualJourneyData> map = new HashMap<>();
		userGroupsRank.stream().forEach(e -> {
			map.put(e.getJourneyId(), e);
		});

		groups.stream().forEach(e -> {
			if (map.containsKey(e.getJourney().getId())) {
				e.setSuccessMatch(CommonUtils.format(map.get(e.getJourney().getId()).getSuccessMatch()));
				e.setJourneyTime(CommonUtils.formatTime(map.get(e.getJourney().getId()).getJourneyTime()));
			}

		});
	}

	@Override
	public void fillSuccessMatch(JournyResponse journyResponse, HttpServletRequest httpRequest) {
		if (journyResponse == null)
			return;
		Account account = authService.getLoginUserAccount(true, httpRequest);
		ManualJourneyData journeyRank = olapManualJourneyRepository.getGroupRankByIds(account.getId(),
				journyResponse.getJourney().getId());
		if (journeyRank != null) {
			journyResponse.setSuccessMatch(CommonUtils.format(journeyRank.getSuccessMatch()));
			journyResponse.setJourneyTime(CommonUtils.formatTime(journeyRank.getJourneyTime()));
		}
	}
}
