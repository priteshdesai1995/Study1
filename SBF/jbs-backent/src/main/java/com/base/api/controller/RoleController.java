/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.controller;

import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.Constants.SPACE;
import static com.base.api.constants.PermissionConstants.PRIVILEGE_MANAGE;
import static com.base.api.constants.PermissionConstants.ROLE_MANAGE;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.UserRole;
import com.base.api.request.dto.ModuleDTO;
import com.base.api.request.dto.RolesDTO;
import com.base.api.service.PrivilegeServices;
import com.base.api.service.RoleService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller contains API Endpoints to manage role and it's permission.
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Api(tags = "Role Management API", description = "Rest APIs for the role management")
@RequestMapping("/role")
@RestController
public class RoleController {

	@Autowired
	RoleService roleService;

	@Autowired
	private PrivilegeServices privilegeServices;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * This endpoint creates a role.
	 * 
	 * @param rolesDTO request dto of role.
	 * @return object of role.
	 * @throws Exception
	 */
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API to add the role", notes = PERMISSION + ROLE_MANAGE, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE })
	public ResponseEntity<TransactionInfo> addRole(@Valid @RequestBody RolesDTO rolesDTO) {
		log.info("RoleController: addRole");

		UserRole userRole = roleService.addRole(rolesDTO);

		if (userRole != null) {
			return ResponseBuilder.buildCRUDResponse(userRole, resourceBundle.getString("role_created"), HttpStatus.OK);
		}
		log.info("RoleController: role added successful");

		return ResponseBuilder.buildInternalServerErrorResponse(userRole,
				resourceBundle.getString("role_create_issue"));
	}

	/**
	 * This endpoint gets all the role with permission.
	 * 
	 * @return list of role.
	 */
	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To get all roles with permission", notes = PERMISSION + ROLE_MANAGE, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE })
	public ResponseEntity<TransactionInfo> getRoles() {
		log.info("RoleController: getRoles");

		List<UserRole> result = roleService.getAllRoles();

		log.info("RoleController: getRoles successful");
		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * This endpoint updates a role.
	 * 
	 * @param roleId  uuid of role.
	 * @param roleDTO request dto of role.
	 * @return object of role.
	 */
	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To update role", notes = PERMISSION + ROLE_MANAGE, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE })
	public ResponseEntity<TransactionInfo> updateRole(@RequestParam(name = "roleId", required = true) String roleId,
			@RequestBody RolesDTO roleDTO) {
		log.info("RoleController: updateRole {}", roleId);

		String result = roleService.updateRole(UUID.fromString(roleId), roleDTO);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("role_updated"), HttpStatus.OK);
		} else {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("role_update_failed"),
					HttpStatus.OK);
		}

	}

	/**
	 * This endpoint gets a role by name.
	 * 
	 * @param roleName is role name.
	 * @return object of role.
	 */
	@GetMapping(value = "/get/name/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To get role", notes = PERMISSION + ROLE_MANAGE, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE })
	public ResponseEntity<TransactionInfo> findByRoleName(@PathVariable("roleName") String roleName) {

		log.info("RoleController: findByRoleName {}", roleName);

		UserRole result = roleService.findByRoleName(roleName);

		log.info("RoleController: findByRoleName {} successful", roleName);

		if (result != null)
			return ResponseBuilder.buildOkResponse(result);
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * This endpoint gets the permissions of the role
	 * 
	 * @param roleId role id.
	 * @return list of permission.
	 */
	@PostMapping(value = "/permission/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To get permission of role", notes = PERMISSION + ROLE_MANAGE + SPACE
			+ PRIVILEGE_MANAGE, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE, PRIVILEGE_MANAGE })
	public ResponseEntity<TransactionInfo> getPermissionList(@RequestBody Map<String, String> request) {

		List<ModuleDTO> result = privilegeServices.getPermissionList(request);

		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * This endpoint assigns permission to the role.
	 * 
	 * @param roleId        uuid of role.
	 * @param permissionIds set of uuid of permission.
	 * @return list of permission
	 */
	@PostMapping(value = "/permission/role_assign", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To assign permission to the role", notes = PERMISSION + ROLE_MANAGE + SPACE
			+ PRIVILEGE_MANAGE, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE, PRIVILEGE_MANAGE })
	public ResponseEntity<TransactionInfo> updateRolePermission(
			@RequestParam(name = "role_id", required = true) String roleId, @RequestBody Map<String, String> request) {
		log.info("RoleController: updateRolePermission {}", roleId);

		String result = roleService.updateRolePermission(UUID.fromString(roleId), request);
		log.info("RoleController: updateRolePermission {} successful", roleId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("role_permission_updated"),
					HttpStatus.OK);
		} else {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("role_permission_update_failed"),
					HttpStatus.OK);
		}

	}

	@PutMapping(value = "/change_status", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To assign permission to the role", notes = PERMISSION + ROLE_MANAGE + SPACE
			+ PRIVILEGE_MANAGE, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE, PRIVILEGE_MANAGE })
	public ResponseEntity<TransactionInfo> changeRoleStatus(@RequestBody Map<String, String> statusReq) {

		String result = roleService.updateStatus(statusReq);

		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));

	}

}