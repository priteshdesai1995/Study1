package com.humaine.portal.api.model;

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
public class AccountAdminDetailsNotExist extends RegistrationRequestAdmin {

	Boolean status;

	public AccountAdminDetailsNotExist(Account acc) {
		super(acc);
	}

}
