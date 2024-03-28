package com.base.api.request.dto;

import java.io.Serializable;
import java.util.UUID;

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
public class ChangePasswordDto implements Serializable {

	private static final long serialVersionUID = 485501938106256685L;

//	@NotEmpty(message = "old password should not be null.")
	// @Size(min = 8, max = 20, message = "old password length should between 8 to
	// 20 character")
	private String oldPassword;

	@NotEmpty(message = "new password should not be null.")
	@Size(min = 8, max = 20, message = "new password length should between 8 to 20 character")
	// @Pattern(regexp =
	// "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{8,20}$",
	// message = "Invalid new password as per norms.")
	private String newPassword;

	@NotEmpty(message = "confirm password should not be null.")
	@Size(min = 8, max = 20, message = "confirm password length should between 8 to 20 character")
	// @Pattern(regexp =
	// "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
	// message = "Invalid confirm password as per norms.")
	private String confirmPassword;

	private UUID userId;
}
