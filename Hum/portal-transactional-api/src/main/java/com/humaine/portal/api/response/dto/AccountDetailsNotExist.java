package com.humaine.portal.api.response.dto;

import java.util.HashMap;
import java.util.Map;

import com.humaine.portal.api.enums.FrontEndMenu;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.request.dto.RegistrationRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountDetailsNotExist extends RegistrationRequest {

	Boolean status;

	Map<FrontEndMenu, Long> menuCounts = new HashMap<FrontEndMenu, Long>();
	
	public AccountDetailsNotExist(Account acc) {
		super(acc);
	}
}
