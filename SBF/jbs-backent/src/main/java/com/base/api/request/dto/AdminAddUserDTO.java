package com.base.api.request.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldNameConstants
@Builder
public class AdminAddUserDTO {
	@NotEmpty(message = "First name can't be null or empty")
	private String firstName;
	@NotEmpty(message = "Last name can't be null or empty")
	private String lastName;
	@NotEmpty(message = "User name can't be null or empty")
	private String userName;
//	@NotEmpty(message = "Must be provide password")
//	@Size(min = 8, max = 32, message = "Password must be between 2 and 32 characters long")
	private String password;
	private LocalDate dateOfBirth;
	private String gender;
	@NotEmpty(message = "Must be provide email")
	private String email;
	private String cellPhone;
	private String roleName;
}
