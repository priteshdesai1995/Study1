package com.base.api.request.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResetPassword implements Serializable {

	private static final long serialVersionUID = -4321182594576696038L;

	@NotEmpty(message = "new password should not be null.")
	@Size(min = 8, max = 20, message = "new password length should between 8 to 20 character")
	private String password;

	@NotEmpty(message = "confirm password should not be null.")
	@Size(min = 8, max = 20, message = "confirm password length should between 8 to 20 character")
	private String confirmPassword;

}
