package com.humaine.portal.api.request.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Signout {

	@NotNull(message = "{api.error.user.registration.email.null}{error.code.separator}{api.error.user.registration.email.null.code}")
	@Email(message = "{api.error.user.registration.email.invalid}{error.code.separator}{api.error.user.registration.email.invalid.code}")
	private String email;

}