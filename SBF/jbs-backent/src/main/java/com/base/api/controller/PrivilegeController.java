/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.controller;

import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.Constants.SPACE;
import static com.base.api.constants.PermissionConstants.PRIVILEGE_MANAGE;
import static com.base.api.constants.PermissionConstants.ROLE_MANAGE;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.Privilege;
import com.base.api.service.PrivilegeServices;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller contains API Endpoints to manage Privilege
 * 
 * @author minesh_prajapati
 *
 */
@Slf4j
@Api(tags = "Privilege Management API", description = "Rest APIs for the privilege management")
@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

	@Autowired
	private PrivilegeServices privilegeServices;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * This endpoint gets all the privilege.
	 * 
	 * @return list of privilege.
	 */
	@ApiOperation(value = "To get all privilege", notes = PERMISSION + ROLE_MANAGE + SPACE
			+ PRIVILEGE_MANAGE, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ROLE_MANAGE, PRIVILEGE_MANAGE })
	@GetMapping(value = "/list", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> getPrivileges() {
		log.info("PrivilegeController: getPrivileges");
		List<Privilege> privileges = privilegeServices.getAllPrivilege();
		log.info("RoleController: getPrivileges successful");
		return ResponseBuilder.buildCRUDResponse(privileges, resourceBundle.getString("subscription_updated"),
				HttpStatus.OK);
	}

}
