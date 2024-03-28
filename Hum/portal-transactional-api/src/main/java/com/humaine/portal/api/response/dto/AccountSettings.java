package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountSettings {
	String apiKey;

	public AccountSettings(Account account) {
		if (account == null)
			return;
		this.apiKey = account.getApiKey();
	}
}
