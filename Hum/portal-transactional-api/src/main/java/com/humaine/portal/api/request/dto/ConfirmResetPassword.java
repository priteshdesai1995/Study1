package com.humaine.portal.api.request.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humaine.portal.api.util.ValidatorPatterns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmResetPassword {
	@JsonIgnore
	private String username;
	
	@NotNull(message = "{api.error.user.registration.email.null}{error.code.separator}{api.error.user.registration.email.null.code}")
	@Email(message = "{api.error.user.registration.email.invalid}{error.code.separator}{api.error.user.registration.email.invalid.code}")
	private String email;
	
	@NotNull(message = "{api.error.user.registration.otp.null}{error.code.separator}{api.error.user.registration.otp.null.code}")
	@Pattern(regexp = ValidatorPatterns.otp, message = "{api.error.user.registration.otp.invalid}{error.code.separator}{api.error.user.registration.otp.invalid.code}")
	private String code;
	
	@NotBlank(message = "{api.error.user.registration.password.null}{error.code.separator}{api.error.user.registration.password.null.code}")
	private String newPassword;
}
