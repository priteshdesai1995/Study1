package com.base.api.request.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.base.api.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolesDto implements Serializable {

	private static final long serialVersionUID = -7780638810105672362L;

	@NotEmpty(message = "Role name can't be null or empty")
	private String name;
//	@JsonIgnore
	private UserStatus status;

}
