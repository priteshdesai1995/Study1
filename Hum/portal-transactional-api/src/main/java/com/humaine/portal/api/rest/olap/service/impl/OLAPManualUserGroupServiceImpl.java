package com.humaine.portal.api.rest.olap.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.model.ManualUserGroupRank;
import com.humaine.portal.api.response.dto.UserGroupListResponse;
import com.humaine.portal.api.response.dto.UserGroupResponse;
import com.humaine.portal.api.rest.olap.repository.OLAPManualUserGroupRepository;
import com.humaine.portal.api.rest.olap.service.OLAPManualUserGroupService;
import com.humaine.portal.api.util.CommonUtils;

@Service
public class OLAPManualUserGroupServiceImpl implements OLAPManualUserGroupService {

	@Autowired
	private OLAPManualUserGroupRepository manualUserGroupRepository;

	@Override
	public void fillSuccessMatch(List<UserGroupResponse> groups, Long account) {
		if (groups == null || groups != null && groups.size() == 0)
			return;
		List<ManualUserGroupRank> userGroupsRank = manualUserGroupRepository.getGroupsRankByIds(account,
				groups.stream().map(e -> e.getGroupId()).collect(Collectors.toList()));
		Map<Long, ManualUserGroupRank> map = new HashMap<>();
		userGroupsRank.stream().forEach(e -> {
			map.put(e.getGroupId(), e);
		});
		
		groups.stream().forEach(e -> {
			if (map.containsKey(e.getGroupId())) {
				e.setSuccessMatch(map.get(e.getGroupId()).getSuccessMatch());
			}
		});
	}

	@Override
	public void fillSuccessMatchForList(List<UserGroupListResponse> groups, Long account) {
		if (groups == null || groups != null && groups.size() == 0)
			return;
		List<ManualUserGroupRank> userGroupsRank = manualUserGroupRepository.getGroupsRankByIds(account,
				groups.stream().map(e -> e.getGroupId()).collect(Collectors.toList()));
		Map<Long, ManualUserGroupRank> map = new HashMap<>();
		userGroupsRank.stream().forEach(e -> {
			map.put(e.getGroupId(), e);
		});
		
		groups.stream().forEach(e -> {
			if (map.containsKey(e.getGroupId())) {
				e.setSuccessMatch(CommonUtils.format(map.get(e.getGroupId()).getSuccessMatch()));
				e.setUserCount(map.get(e.getGroupId()).getNoOfUsers());
			}
		});
	}
}
