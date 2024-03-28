package com.humaine.portal.api.rest.service;

import com.humaine.portal.api.model.Account;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

	Account getLoginUserAccount(HttpServletRequest httpRequest);
	
	Account getLoginUserAccount(Boolean validate, HttpServletRequest httpRequest);
}
