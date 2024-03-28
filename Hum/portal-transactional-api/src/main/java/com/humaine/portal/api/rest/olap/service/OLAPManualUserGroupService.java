package com.humaine.portal.api.rest.olap.service;

import java.util.List;

import com.humaine.portal.api.response.dto.UserGroupListResponse;
import com.humaine.portal.api.response.dto.UserGroupResponse;

public interface OLAPManualUserGroupService {

	void fillSuccessMatch(List<UserGroupResponse> groups, Long account);
	
	void fillSuccessMatchForList(List<UserGroupListResponse> groups, Long account);
}
