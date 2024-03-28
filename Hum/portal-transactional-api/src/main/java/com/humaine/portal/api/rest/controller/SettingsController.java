package com.humaine.portal.api.rest.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.response.dto.AccountSettings;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.Constants;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.SecretKeyUtils;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Account Settings", description = "User Account Settings")
@RequestMapping("account/settings")
public class SettingsController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private SecretKeyUtils secretKeyUtils;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get Account Settings", notes = "get Account Settings", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getAccountInfo(HttpServletRequest httpRequest) throws Exception {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		// Account account = accountRepository.findAccountByEmail(auth.getName());
		Account account;
		if(auth.getName().equals(Constants.ADMIN_USER_EMAIL) && httpRequest.getParameterMap().containsKey(Constants.USERNAME_KEY) && httpRequest.getParameter(Constants.USERNAME_KEY)!="") {
			account = accountRepository.findAccountByEmail(httpRequest.getParameter(Constants.USERNAME_KEY));
		} else {
			account = accountRepository.findAccountByEmail(auth.getName());
		}
		return ResponseBuilder.buildResponse(new AccountSettings(account));
	}

	@PutMapping(value = "api-key", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update Account Info", notes = "update Account Info", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> updateAccountApiKey(HttpServletRequest httpRequest) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		// Account account = accountRepository.findAccountByEmail(auth.getName());
		Account account;
		if(auth.getName().equals(Constants.ADMIN_USER_EMAIL) && httpRequest.getParameterMap().containsKey(Constants.USERNAME_KEY) && httpRequest.getParameter(Constants.USERNAME_KEY)!="") {
			account = accountRepository.findAccountByEmail(httpRequest.getParameter(Constants.USERNAME_KEY));
		} else {
			account = accountRepository.findAccountByEmail(auth.getName());
		}
		String key = secretKeyUtils.generateSecretKey(account.getEmail(), account.getUsername());
		if (!StringUtils.isBlank(key)) {
			account.setApiKey(key);
		}
		accountRepository.save(account);
		return ResponseBuilder.buildResponse(new AccountSettings(account));
	}
}
