package com.humaine.admin.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolesDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4798473067070857591L;

	// @NotEmpty(message = "Role name can't be null or empty")
	private String roleName;

	private String status;
}
