package com.base.api.service;

import java.util.List;

import com.base.api.request.dto.PermissionDTO;

public interface PermissionService {

	public void createPermission(PermissionDTO permissionDTO);
	
	public void deletePermission(List<String> permissionId);
	
}
