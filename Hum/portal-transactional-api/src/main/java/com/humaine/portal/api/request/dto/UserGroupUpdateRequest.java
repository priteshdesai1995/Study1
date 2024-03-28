package com.humaine.portal.api.request.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupUpdateRequest {

	@NotBlank(message = "{api.error.user-group.name.null}{error.code.separator}{api.error.user-group.name.null.code}")
	String name;
}
