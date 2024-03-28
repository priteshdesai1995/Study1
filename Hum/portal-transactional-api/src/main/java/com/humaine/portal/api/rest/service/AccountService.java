package com.humaine.portal.api.rest.service;

import com.humaine.portal.api.model.Account;

public interface AccountService {
	Account getAccountByEmail(String email);
}
