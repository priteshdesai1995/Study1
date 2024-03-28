package com.humaine.admin.api.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.humaine.admin.api.dto.RolesDTO;
import com.humaine.admin.api.mapper.MapperUserService;
import com.humaine.admin.api.service.RoleService;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.UserRole;
import com.humaine.portal.api.rest.repository.RoleAdminRepository;
import com.humaine.portal.api.util.ErrorMessageUtils;

@Service
public class RoleServiceImpl implements RoleService {

	private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	RoleAdminRepository roleRepository;

	@Autowired
	MapperUserService mapperUserService;

	@Autowired
	ErrorMessageUtils errorMessageUtils;

	@Override
	public String addRole(RolesDTO roleDto) {
		try {
			ExampleMatcher name_matcher = ExampleMatcher.matching().withMatcher("role_name",
					GenericPropertyMatchers.ignoreCase());
			Example<UserRole> example = Example.<UserRole>of(new UserRole(null, roleDto.getRoleName(), null, null),
					name_matcher);
			if (!roleRepository.exists(example)) {
				UserRole roleEntityAfterSave = roleRepository.save(mapperUserService.mapRoleFromDto(roleDto));
				if (roleEntityAfterSave != null && roleEntityAfterSave.getRoleId() != null) {
					return HttpStatus.OK.name();
				} else {
					return "Failed to Save Role Due to database connectivity problem.";
				}
			} else {
				return "Given role is already registered.";
			}

		} catch (Exception ex) {
			log.error("Failed to save role :: " + ex.getMessage());
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.role.save", new Object[] {},
					"api.error.role.save.code"));
		}

	}
}
