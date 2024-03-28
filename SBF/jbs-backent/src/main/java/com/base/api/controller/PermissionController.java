package com.base.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.constants.Constants;
import com.base.api.request.dto.PermissionDTO;
import com.base.api.service.PermissionService;
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

@RequestMapping("/permission")
@Api(tags = "Permission Management API", description = "Rest APIs for the permission management") // This annotation is
																									// for swagger ui
@Slf4j
@RestController
public class PermissionController {

	private PermissionService permissionService;

	@PostMapping(value = "create-permission")
	@ApiOperation(value = "API Endpoint for creating permissions", authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	public ResponseEntity<TransactionInfo> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {

		log.info("PermissionController : createPermission");
		permissionService.createPermission(permissionDTO);
		log.info("Permission Created successfully");
		return ResponseBuilder.buildMessageCodeResponse("permission-created", "200", HttpStatus.OK);
	}

	@DeleteMapping(value = "delete-permission")
	@ApiOperation(value = "API Endpoint for deleting permissions", authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	public ResponseEntity<TransactionInfo> deletePermission(@RequestParam("permissionId") List<String> permissions) {
		log.info("PermissionController : createPermission");
		permissionService.deletePermission(permissions);
		log.info("Permission deleted successfully");
		return ResponseBuilder.buildMessageCodeResponse("permission-deleted", "200", HttpStatus.OK);
	}
}
