/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.api.constants.SecurityConstants;
import com.base.api.entities.ModuleEntity;
import com.base.api.entities.ModuleRoleEntity;
import com.base.api.entities.User;
import com.base.api.entities.UserRole;
import com.base.api.enums.UserStatus;
import com.base.api.exception.APIException;
import com.base.api.exception.UserRoleException;
import com.base.api.repository.ModuleRepository;
import com.base.api.repository.ModuleRoleRepository;
import com.base.api.repository.UserRoleRepository;
import com.base.api.request.dto.RolesDTO;
import com.base.api.service.PrivilegeServices;
import com.base.api.service.RoleService;
import com.base.api.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * This class implements services for Privileges.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private UserRoleRepository roleRepository;

	@Autowired
	private PrivilegeServices privilegeServices;


	@Autowired
	ModuleRoleRepository moduleRoleRepository;

	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	@Lazy
	TokenStore tokenStore;

	@Override
	public List<UserRole> getAllRoles() {
		log.info("RoleServiceImpl: Start getAllRoles");
		List<UserRole> userRoles = roleRepository.findAll();
		if (userRoles == null || userRoles.isEmpty()) {
			log.error("RoleServiceImpl: no roles found");
			throw new APIException("role.not.found", HttpStatus.NOT_FOUND);
		}
		log.info("RoleServiceImpl: End getAllRoles successful");
		return userRoles;
	}

	@Override
	public UserRole findByRoleId(UUID roleId) {
		log.info("RoleServiceImpl: findByRoleId {}", roleId);
		return roleRepository.findById(roleId).orElseThrow(() -> {
			log.error("RoleServiceImpl: findByRoleId {} not found", roleId);
			throw new APIException("role.not.found", HttpStatus.NOT_FOUND);
		});
	}

	@Override
	public UserRole findByRoleName(String roleName) {
		log.info("RoleServiceImpl: Start findByRoleName {}", roleName);
		UserRole userRole = roleRepository.findByRoleName(roleName);
		if (userRole == null) {
			log.error("RoleServiceImpl: findByRoleName {} not found", roleName);
			throw new APIException("role.not.found", HttpStatus.NOT_FOUND);
		}
		log.info("RoleServiceImpl: End findByRoleName {}", roleName);
		return userRole;
	}

	@Override
	@Transactional
	public UserRole addRole(RolesDTO roleDto) {
		log.info("RoleServiceImpl: Start addRole");
		ExampleMatcher name_matcher = ExampleMatcher.matching().withMatcher("role_name",
				GenericPropertyMatchers.ignoreCase());
		Example<UserRole> example = Example.<UserRole>of(new UserRole(null, roleDto.getName(), null), name_matcher);
		if (!roleRepository.exists(example)) {
			UserRole role = roleRepository.save(new UserRole(roleDto.getName(), UserStatus.INACTIVE, null));
			if (role == null) {
				log.error("RoleServiceImpl: addRole fail");
				throw new APIException("role.create.fail", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			log.info("RoleServiceImpl: End addRole");
			return role;
		} else {
			log.error("RoleServiceImpl: addRole already exist");
			throw new APIException("role.already.exist", HttpStatus.CONFLICT);
		}
	}

	@Override
	@Transactional
	public String updateRole(UUID roleId, RolesDTO roleDTO) {
		log.info("RoleServiceImpl: updateRole {}", roleId);
		UserRole userRole = findByRoleId(roleId);
		if (roleDTO.getName() != null && !roleDTO.getName().isBlank()) {
			userRole.setRoleName(roleDTO.getName());
		}
		if (roleDTO.getStatus() != null && !roleDTO.getStatus().isBlank()) {
			userRole.setStatus(UserStatus.valueOfCode(roleDTO.getStatus()));
		}
		// userRole.setUpdatedDate(LocalDateTime.now());
		userRole = roleRepository.save(userRole);
		if (userRole == null) {
			log.error("RoleServiceImpl: updateRole fail {}", roleId);
			throw new APIException("role.update.fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("RoleServiceImpl: updateRole successful {}", roleId);
		return HttpStatus.OK.name();
	}

	@Override
	@Transactional
	public String updateRolePermission(UUID roleId, Map<String, String> request) {
		
		log.info("RoleServiceImpl: Start updateRolePermission {}", roleId);
		
		UserRole userRole = findByRoleId(roleId);
		
		if (userRole == null)
		{
			log.error("NO user role found");
			HttpStatus.NOT_FOUND.name();
		}
		
		List<ModuleRoleEntity> moduleRoleEntities = moduleRoleRepository.findByRole(userRole);
		
		for (ModuleRoleEntity moduleRoleEntity : moduleRoleEntities) {
			moduleRoleRepository.delete(moduleRoleEntity);
		}
		
		if (!request.get("permission_key").isEmpty()) {
			ModuleRoleEntity moduleRoleEntity = null;
			for (String moduleId : request.get("permission_key").split(",")) {

				moduleRoleEntity = new ModuleRoleEntity();

				ModuleEntity moduleEntity = moduleRepository.findById(UUID.fromString(moduleId)).get();
				
				moduleRoleEntity.setModule(moduleEntity);
				moduleRoleEntity.setRole(userRole);
				moduleRoleEntity.setCreatedDate(LocalDateTime.now());

				moduleRoleRepository.save(moduleRoleEntity);
			}
		}
		return HttpStatus.OK.name();

	}

	/**
	 * This method is use for revoke access token of all user base on role.
	 * 
	 * @param roleId uuid of role.
	 */
	private void removeAccessTokenByRoleId(UUID roleId) {
		log.info("RoleServiceImpl: Start removeAccessTokenByRoleId {}", roleId);
		List<User> users = userService.findUserByRoleId(roleId);
		for (User user : users) {
			Collection<OAuth2AccessToken> tokens = tokenStore
					.findTokensByClientIdAndUserName(SecurityConstants.CLIENT_ID, user.getUserName());
			if (tokens != null) {
				for (OAuth2AccessToken token : tokens) {
					tokenStore.removeAccessToken(token);
				}
			}
		}
		log.info("RoleServiceImpl: End removeAccessTokenByRoleId {}", roleId);
	}

	@Override
	public void createRole(RolesDTO rolesDTO) throws Exception {
		log.info("RoleServiceImpl : createRole");
		
		try {
			UserRole role = new UserRole(rolesDTO.getName(), UserStatus.valueOf(rolesDTO.getStatus()));			
			log.info("User role : "+ role.toString() +" will be saved in db ");
			roleRepository.save(role);
			
		}catch(Exception exception) {
			log.info("Exception while creating Role : "+ exception);
			throw new Exception("Exception while creating Role : "+ exception);
		}
	}

	@Override
	public void deleteRole(String roleId) {
		try {
			log.info("RoleServiceImpl : deleteRole");
			UserRole userRole = getUserRoleById(roleId);
			
			if(null == userRole || null == userRole.getId()) {
				log.error("user role is null");
				throw new UserRoleException("user.role.not.found", HttpStatus.NOT_FOUND);
			}
			
			roleRepository.deleteById(UUID.fromString(roleId));
			log.info("User Role with roleId : "+ roleId + " deleted.");
			
		}catch (UserRoleException roleException) {
			
			log.error("some exception while deleting the role "+ roleException);
			throw new UserRoleException("some.exception.while.deleting.the.role", HttpStatus.INTERNAL_SERVER_ERROR);
			
		}catch (Exception e) {
			
			log.error("some exception while deleting the role "+ e);
			throw new UserRoleException("some.exception.while.deleting.the.role", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Override
	public String updateStatus(Map<String, String> statusReq) {
		
		UserRole userRole = roleRepository.findById(UUID.fromString(statusReq.get("roleId"))).get();

		if (userRole == null)
			return HttpStatus.NOT_FOUND.name();
		
		userRole.setStatus(UserStatus.valueOf(statusReq.get("status")));
		roleRepository.save(userRole);

		return HttpStatus.OK.name();
		
	}
	
	@Override
	public void changeStatus(String roleId, String status) {

		if(roleId != null) {
			Optional<UserRole> userRole = roleRepository.findById(UUID.fromString(roleId));
			
			if(userRole.isPresent()) {
				UserRole role = userRole.get();
				role.setStatus(UserStatus.valueOf(status));
				roleRepository.save(role);
				log.info("Status changed successfully ");
				
			}else {
				log.error("No Role found with : "+roleId);
				throw new UserRoleException("no.role.found.with.this.roleid", HttpStatus.NOT_FOUND);
			}
		}else {
			log.error("Role ID is null ");
			throw new UserRoleException("roleId.can.not.be.null", HttpStatus.BAD_REQUEST);
		}
	}

	public UserRole getUserRoleById(String roleId) {
		
		if(roleId != null) {
			Optional<UserRole> userRole = roleRepository.findById(UUID.fromString(roleId));
			
			if(userRole.isPresent()) {
				return userRole.get();
			}else {
				log.error("No Role found with : "+roleId);
				throw new UserRoleException("no.role.found.with.this.roleid", HttpStatus.NOT_FOUND);
			}
		}else {
			log.error("Role ID is null ");
			throw new UserRoleException("roleId.can.not.be.null", HttpStatus.BAD_REQUEST);
		}
	}
}
