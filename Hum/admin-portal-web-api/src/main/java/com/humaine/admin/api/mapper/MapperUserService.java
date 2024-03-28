package com.humaine.admin.api.mapper;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.humaine.admin.api.dto.RolesDTO;
import com.humaine.admin.api.model.UserRole;
import com.humaine.admin.api.util.DateUtils;

@Component
public class MapperUserService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 578703711863554565L;

	public UserRole mapRoleFromDto(RolesDTO roleDto) {
		UserRole userRole = new UserRole();
		userRole.setRoleName(roleDto.getRoleName());
		userRole.setStatus("Active");
		userRole.setCreateDate(DateUtils.getCurrentTimestemp());
		return userRole;
	}

}
