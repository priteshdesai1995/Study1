package com.base.api.service;

import java.util.List;

public interface RolePermissionService {

	public void addPermissionToRole(String roleId, List<String> permissionIds);
	
	public void removePermissionFromRole(String roleId, List<String> permissionIds);
	
}
