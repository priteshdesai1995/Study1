package com.humaine.portal.api.request.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignInRequest {

	@NotNull(message = "{api.error.user.registration.email.null}{error.code.separator}{api.error.user.registration.email.null.code}")
	@Email(message = "{api.error.user.registration.email.invalid}{error.code.separator}{api.error.user.registration.email.invalid.code}")
	private String email;

	@JsonIgnore
	private String username;

	@NotBlank(message = "{api.error.user.registration.password.null}{error.code.separator}{api.error.user.registration.password.null.code}")
	private String password;

	@JsonIgnore
	private String accessToken;
}
