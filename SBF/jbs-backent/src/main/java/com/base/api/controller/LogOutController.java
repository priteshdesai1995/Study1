package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.USER_LOGOUT;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.utils.ErrorStatus;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LogOutController {
	
	@Autowired
	TokenStore tokenStore;
	
	@ApiOperation(value = "Api for User logout", notes = PERMISSION + USER_LOGOUT, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { USER_LOGOUT })
	@PostMapping(value = "/logout")
	public ResponseEntity<TransactionInfo> userLogOut(HttpServletRequest request) {
		
		log.info("LogoutController : userLogOut");
		
		String authHeader =request.getHeader("Authorization") == null  ? request.getHeader("authorization")
				: request.getHeader("Authorization");
		
		if(authHeader != null) {
			
			String accessTokenizer[] = authHeader.split(" ");
			
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenizer[1]);
			tokenStore.removeAccessToken(accessToken);
		}
		
		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.SUCCESS);
		transactionInfo.setStatusCode(HttpStatus.ACCEPTED.value());
		return new ResponseEntity<>(transactionInfo, HttpStatus.ACCEPTED);

	}

}
