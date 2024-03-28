package com.base.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.constants.Constants;
import com.base.api.service.RolePermissionService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author preyansh_prajapati
 * 
 *         THis is the controller class that will have some methods that
 *         excexutes when API is called
 * 
 */

@RequestMapping("/role-permission")
@Api(tags = "Role-Permission Management API", description = "Rest APIs for the role permission management") // This
@Slf4j
@RestController
public class RolePermissionController {

	private RolePermissionService rolePermissionService;

	@PutMapping(value = "add-permission-to-role")
	@ApiOperation(value = "API Endpoint for adding permission to the role", authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	public ResponseEntity<TransactionInfo> addPermissionToRole(@RequestParam("roleId") String roleId,
			@RequestParam("permissionIds") List<String> permissionIds) {

		log.info("RolePermissionController  :  addPermissionToRole");
		rolePermissionService.addPermissionToRole(roleId, permissionIds);

		return ResponseBuilder.buildWithMessage("permissions.removed.successfully");
	}

	@PutMapping(value = "remove-permission-from-role")
	@ApiOperation(value = "API Endpoint for remove permission from role", authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	public ResponseEntity<TransactionInfo> removePermissionFromRole(@RequestParam("roleId") String roleId,
			@RequestParam("permissionIds") List<String> permissionIds) {

		log.info("RolePermissionController  :  removePermissionFromRole");
		rolePermissionService.removePermissionFromRole(roleId, permissionIds);

		return ResponseBuilder.buildWithMessage("permissions.removed.successfully");
	}

}
