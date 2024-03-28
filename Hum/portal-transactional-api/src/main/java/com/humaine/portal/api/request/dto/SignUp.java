package com.humaine.portal.api.request.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.humaine.portal.api.util.ValidatorPatterns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {

	@NotBlank(message = "{api.error.user.registration.username.null}{error.code.separator}{api.error.user.registration.username.null.code}")
	private String username;

	@NotNull(message = "{api.error.user.registration.email.null}{error.code.separator}{api.error.user.registration.email.null.code}")
	@Email(message = "{api.error.user.registration.email.invalid}{error.code.separator}{api.error.user.registration.email.invalid.code}")
	private String email;

	@NotBlank(message = "{api.error.user.registration.password.null}{error.code.separator}{api.error.user.registration.password.null.code}")
	private String password;
}
