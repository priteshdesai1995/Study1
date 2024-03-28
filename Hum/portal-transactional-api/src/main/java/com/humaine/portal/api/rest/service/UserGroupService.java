package com.humaine.portal.api.rest.service;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.UserGroup;
import com.humaine.portal.api.request.dto.UserGroupRequest;
import com.humaine.portal.api.request.dto.UserGroupUpdateRequest;
import com.humaine.portal.api.response.dto.Persona;
import javax.servlet.http.HttpServletRequest;

public interface UserGroupService {

	void checkUserGroupNameExist(Account account, String groupName);

	void checkUserGroupNameExist(Account account, Long groupId, String groupName);

	UserGroup createUserGroup(UserGroupRequest request, HttpServletRequest httpRequest);

	UserGroup updateUserGroup(UserGroupRequest request, Long groupId, HttpServletRequest httpRequest);

	void validateRequest(UserGroupRequest request);

	Persona getPersonaDetails(Long groupId, HttpServletRequest httpRequest);

	void updateUserGroup(UserGroupUpdateRequest request, Long groupId, HttpServletRequest httpRequest);
}
