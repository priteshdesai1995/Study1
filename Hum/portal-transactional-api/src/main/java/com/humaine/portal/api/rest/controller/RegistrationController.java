package com.humaine.portal.api.rest.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.enums.FrontEndMenu;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.request.dto.RegistrationRequest;
import com.humaine.portal.api.response.dto.AccountDetailsNotExist;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.repository.TestJourneyRepository;
import com.humaine.portal.api.rest.repository.UserRepository;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.Constants;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.SecretKeyUtils;
import com.humaine.portal.api.util.TrackerGenerator;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "register", description = "User Save Account Information")
@RequestMapping("register")
public class RegistrationController {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private TrackerGenerator trackerGenerator;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TestJourneyRepository testJourneyRepository;

	@Autowired
	private SecretKeyUtils secretKeyUtils;

	@PostMapping(value = "", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Save Account Info", notes = "Save Account Info", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> saveAccountInfo(@Valid @RequestBody RegistrationRequest request)
			throws APIException, WrongRepositoryStateException, InvalidConfigurationException, InvalidRemoteException,
			CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException,
			GitAPIException {
		Account acount = this.validateSignupRequest(request);
		acount.setRegistrationInfo(request);
		String key = secretKeyUtils.generateSecretKey(acount.getEmail(), acount.getUsername());
		if (key != null) {
			acount.setApiKey(key);
		}
		accountRepository.save(acount);
		generateTrackerFile(acount);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.registration.success", null, "api.success.registration.success.code"));
	}

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get Account Info", notes = "get Account Info", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getAccountInfo(HttpServletRequest httpRequest) throws Exception {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		// Account account = accountRepository.findAccountByEmail(auth.getName()); account = accountRepository.findAccountByEmail(auth.getName());ccount account = accountRepository.findAccountByEmail(auth.getName());
		Account account;
		if(auth.getName().equals(Constants.ADMIN_USER_EMAIL) && httpRequest.getParameterMap().containsKey(Constants.USERNAME_KEY) && httpRequest.getParameter(Constants.USERNAME_KEY)!="") {
			account = accountRepository.findAccountByEmail(httpRequest.getParameter(Constants.USERNAME_KEY));
		} else {
			account = accountRepository.findAccountByEmail(auth.getName());
		}
		
		if (account == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}
		if (AccountStatus.unconfirmed.equals(account.getStatus())) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
					new Object[] {}, "api.error.account.confirmation.pending.code"));
		}
		Long count = 0L;
		if (account.getBusinessInformation() == null) {
			Account acc = new Account();
			acc.setId(account.getId());
			AccountDetailsNotExist result = new AccountDetailsNotExist(acc);
			result.getMenuCounts().put(FrontEndMenu.TEST_NEW_JOURNEY, count);
			result.setStatus(false);
			return ResponseBuilder.buildResponse(result);
		}

		count = testJourneyRepository.getJourniesCountByAccount(account.getId());
		AccountDetailsNotExist result = new AccountDetailsNotExist(account);
		result.getMenuCounts().put(FrontEndMenu.TEST_NEW_JOURNEY, count);
		result.setStatus(account.getBusinessInformation() != null);
		return ResponseBuilder.buildResponse(result);
	}

	@PutMapping(value = "", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update Account Info", notes = "update Account Info", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> updateAccountInfo(@Valid @RequestBody RegistrationRequest request, HttpServletRequest httpRequest)
			throws APIException {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		// Account account = accountRepository.findAccountByEmail(auth.getName()); account = accountRepository.findAccountByEmail(auth.getName());ccount account = accountRepository.findAccountByEmail(auth.getName());

		Account account;
		if(auth.getName().equals(Constants.ADMIN_USER_EMAIL) && httpRequest.getParameterMap().containsKey(Constants.USERNAME_KEY) && httpRequest.getParameter(Constants.USERNAME_KEY)!="") {
			account = accountRepository.findAccountByEmail(httpRequest.getParameter(Constants.USERNAME_KEY));
		} else {
			account = accountRepository.findAccountByEmail(auth.getName());
		}
		if (account == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}
		if (AccountStatus.unconfirmed.equals(account.getStatus())) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
					new Object[] {}, "api.error.account.confirmation.pending.code"));
		}
		if (account.getBusinessInformation() == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}
		account.setRegistrationInfo(request);
		accountRepository.save(account);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.registration.success", null, "api.success.registration.success.code"));
	}

	private Account validateSignupRequest(RegistrationRequest request) throws APIException {
		Optional<Account> account = this.accountRepository.findById(request.getAccountID());
		if (account.isEmpty()) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.not.found",
					new Object[] { request.getAccountID() }, "api.error.account.not.found.code"));
		}
		Account acc = account.get();
		if (AccountStatus.unconfirmed.equals(acc.getStatus())) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
					new Object[] { request.getAccountID() }, "api.error.account.confirmation.pending.code"));
		}
		if (acc.getBusinessInformation() != null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user.already.register",
					new Object[] { acc.getEmail() }, "api.error.user.already.register.code"));
		}
		return acc;
	}

	private void generateTrackerFile(Account account) {
		taskExecutor.execute(new TrackerFileGenerator(account));
	}

	private class TrackerFileGenerator implements Runnable {

		Account account;

		public TrackerFileGenerator(Account account) {
			this.account = account;
		}

		@Override
		public void run() {
			try {
				trackerGenerator.genrateTracker(account);
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		}

	}
}
