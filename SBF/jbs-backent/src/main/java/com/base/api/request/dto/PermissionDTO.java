package com.base.api.request.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionDTO implements Serializable{
	 
	 
	private static final long serialVersionUID = -8766467659391556868L;

	@NotNull(message = "permissionName can not be null")
	private String permissionName;
}
