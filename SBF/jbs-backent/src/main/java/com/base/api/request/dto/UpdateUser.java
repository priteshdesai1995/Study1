package com.base.api.request.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUser {
	@NotEmpty(message = "firstName should not be null.")
	private String firstName;
	@NotEmpty(message = "lastName should not be null.")
	private String lastName;
}
