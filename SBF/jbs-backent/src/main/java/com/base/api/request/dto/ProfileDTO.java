package com.base.api.request.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.base.api.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ProfileDTO {
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String cellPhone;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateOfBirth;
	private String gender;
	private UserStatus status;
	private RolesDto roles;
	private UUID profileId;
}
