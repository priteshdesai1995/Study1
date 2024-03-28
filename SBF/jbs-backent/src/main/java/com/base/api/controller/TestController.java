package com.base.api.controller;

import static com.base.api.constants.PermissionConstants.SAMPLE_PERMISSION;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.UserRole;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Test APIs", description = "Test APIs")
@RequestMapping(value = "/test")
public class TestController {

	@ApiOperation(value = "Test API Endpoint", notes = "Test API Endpoint note", authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { SAMPLE_PERMISSION })
	@GetMapping()
	public ResponseEntity<TransactionInfo<UserRole>> getMessage(@RequestParam("msg") String msg) {
		return null;
	}
	@GetMapping(value="/name")
	public String getName(){
		System.out.println("Mahesh");
		return "Welcome";}
		
}