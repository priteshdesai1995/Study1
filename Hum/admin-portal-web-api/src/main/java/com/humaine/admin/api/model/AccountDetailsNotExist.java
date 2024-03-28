package com.humaine.admin.api.model;

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

	public AccountDetailsNotExist(Account acc) {
		super(acc);
	}

}
