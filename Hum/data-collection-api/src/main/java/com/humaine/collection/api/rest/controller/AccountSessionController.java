package com.humaine.collection.api.rest.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.response.dto.AccountSessionResponse;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ResponseBuilder;
import com.humaine.collection.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Timeout", description = "Session Timeout for specific e-comm-site")
@RequestMapping("sessionTimeout")
public class AccountSessionController {
	private static final Logger log = LogManager.getLogger(AccountSessionController.class);

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ErrorMessageUtils errorMessageUtils;

	@Autowired
	UserSessionService userSessionService;

	@ApiOperation(value = "Capture Session Timeout data for Account ID", notes = "Capture Session Timeout data for Account ID", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@GetMapping(path = "{accountID}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<TransactionInfo> getAccountSessionTimeout(@PathVariable("accountID") Long accountID) {

		if (accountID == null) {
			log.error("Empty Account ID: {}", accountID);
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.accountID.null", null,
					"api.error.accountID.null.code"));
		}

		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

		Account account = authentication.getAccount();
//		Optional<Account> account = accountRepository.findById(accountID);
//
//		if (account.isEmpty()) {
//			log.error("Account Not found with Account Id: {}", accountID);
//			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found",
//					new Object[] { accountID }, "api.error.account.not.found.code"));
//		}

		userSessionService.checkRequestedAccountWithAPIKey(accountID);

		log.info("Timeout for Account:{} is => {}", accountID, account.getSessionTimeout());

		AccountSessionResponse response = new AccountSessionResponse();
		response.setTimeout(account.getSessionTimeout());

		return ResponseBuilder.buildResponse(response);
	}
}
