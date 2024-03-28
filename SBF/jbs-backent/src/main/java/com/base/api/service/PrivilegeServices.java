/**
 * Copyright ${year} Brainvire - All rights reserved.
 */
package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.base.api.entities.Privilege;
import com.base.api.request.dto.ModuleDTO;

/**
 * This interface provides services for Privileges.
 * 
 * @author minesh_prajapati
 *
 */
public interface PrivilegeServices {

	List<Privilege> getAllPrivilege();

	List<Privilege> findByRoleID(UUID roleId);

	List<Privilege> findAllByIds(Set<UUID> permissionIds);
	
	public List<ModuleDTO> getPermissionList(Map<String, String> request);
}
