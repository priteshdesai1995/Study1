package com.humaine.portal.api.response.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private Long expiresIn;
	private String actualDate;
	private String expirationDate;
	private String username;
	private String message;
	private Long accountId;

	public AuthenticationResponse(String accessToken, Long expiresIn, String sessionToken) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = sessionToken;
	}
}
