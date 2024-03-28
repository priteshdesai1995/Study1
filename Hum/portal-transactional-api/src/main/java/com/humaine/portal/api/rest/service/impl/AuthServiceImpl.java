package com.humaine.portal.api.rest.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.util.Constants;
import com.humaine.portal.api.util.ErrorMessageUtils;
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Override
	public Account getLoginUserAccount(HttpServletRequest httpRequest) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		// Account account = accountRepository.findAccountByEmail(auth.getName());
		Account account;
		if(auth.getName().equals(Constants.ADMIN_USER_EMAIL) && httpRequest.getParameterMap().containsKey(Constants.USERNAME_KEY) && httpRequest.getParameter(Constants.USERNAME_KEY)!="") {
			account = accountRepository.findAccountByEmail(httpRequest.getParameter(Constants.USERNAME_KEY));
		} else {
			account = accountRepository.findAccountByEmail(auth.getName());
		}
		return account;
	}

	@Override
	public Account getLoginUserAccount(Boolean validate, HttpServletRequest httpRequest) {
		Account account = getLoginUserAccount(httpRequest);
		if (!validate)
			return account;
		if (account == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
					new Object[] {}, "api.error.account.details.not.found.code"));
		}
		if (AccountStatus.unconfirmed.equals(account.getStatus())) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
					new Object[] {}, "api.error.account.confirmation.pending.code"));
		}
		if (account.getBusinessInformation() == null) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.registration.pending",
					new Object[] { account.getId() }, "api.error.registration.pending.code"));
		}
		return account;
	}

}
