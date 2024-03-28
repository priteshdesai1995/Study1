/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.base.api.entities.UserRole;
import com.base.api.request.dto.RolesDTO;

/**
 * This interface provides services for Roles.
 * 
 * @author minesh_prajapati
 *
 */
public interface RoleService {

	List<UserRole> getAllRoles();

	UserRole findByRoleId(UUID roleId);

	UserRole findByRoleName(String roleName);

	UserRole addRole(RolesDTO roleDto);

	String updateRole(UUID role_id, RolesDTO roleDto);
	
	String updateStatus(Map<String, String> statusReq);

	String updateRolePermission(UUID roleId, Map<String, String> request);

	public void createRole(RolesDTO rolesDTO) throws Exception ;
	
	public void deleteRole(String roleId);
	
	public void changeStatus(String roleId, String status);
}
