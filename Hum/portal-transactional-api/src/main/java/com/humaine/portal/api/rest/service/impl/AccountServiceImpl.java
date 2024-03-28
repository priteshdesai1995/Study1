package com.humaine.portal.api.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.rest.service.AccountService;
import com.humaine.portal.api.util.ErrorMessageUtils;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ErrorMessageUtils errorMessageUtils;
	
	@Override
	public Account getAccountByEmail(String email) {
		Account account = this.accountRepository.findAccountByEmail(email);
		if (account == null) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.user.found",
					new Object[] { email }, "api.error.user.found.code"));
		}
		return account;
	}

}
