package com.humaine.portal.api.rest.olap.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.response.dto.MyJourneyAnalysisResponse;
import com.humaine.portal.api.response.dto.MyJourneyDetails;
import com.humaine.portal.api.rest.olap.repository.MyUserGroupMasterBackUpRepository;
import com.humaine.portal.api.rest.olap.service.OLAPMyJourneyService;
import com.humaine.portal.api.rest.service.AuthService;

@Service
public class OLAPMyJourneyServiceImpl implements OLAPMyJourneyService {

	private static final Logger log = LogManager.getLogger(OLAPMyJourneyServiceImpl.class);

	@Autowired
	MyUserGroupMasterBackUpRepository upRepository;

	@Autowired
	AuthService authService;

	@Override
	public void fillGroupNameAndBigFiveForList(List<MyJourneyAnalysisResponse> journeyList) {
		if (journeyList == null || journeyList != null && journeyList.size() == 0)
			return;
		Set<String> groupIds = journeyList.stream().map(p -> p.getGroupId().toString()).collect(Collectors.toSet());
		List<MyJourneyDetails> bigFiveAndGroupName = upRepository.getBigFiveAndGroupName(groupIds);
		Map<String, MyJourneyDetails> map = new HashMap<>();
		bigFiveAndGroupName.stream().forEach(e -> {
			map.put(e.getGroupName(), e);
		});
		journeyList.stream().forEach(e -> {
			if (map.containsKey(e.getGroupId().toString())) {
				e.setBigFive(map.get(e.getGroupId().toString()).getBigFive());
				e.setGroupName(e.getGroupId().toString());
			}
		});
	}

	@Override
	public void fillGroupNameAndBigFive(MyJourneyAnalysisResponse response) {
		if (response == null) {
			return;
		}
		MyJourneyDetails myJourney = upRepository.getBigFiveAndGroupName(response.getGroupId().toString());
		if (myJourney != null) {
			response.setBigFive(myJourney.getBigFive());
			response.setGroupName(myJourney.getGroupName());
		}
	}
}
