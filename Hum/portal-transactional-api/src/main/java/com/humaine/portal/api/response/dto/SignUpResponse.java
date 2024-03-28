package com.humaine.portal.api.response.dto;

import com.humaine.portal.api.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
	private String username;
	private Long accountID;
	private AccountStatus status;

}
