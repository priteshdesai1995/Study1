/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.response.dto;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.base.api.enums.Status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * This class is a response DTO for announcement's user.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@ToString
@EqualsAndHashCode
public class UserResponseDTO implements Serializable {

	private String userName;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String email;
	private String roleName;

	/**
	 * @param userName
	 * @param status
	 * @param email
	 * @param roleName
	 */
	public UserResponseDTO(String userName, Status status, String email, String roleName) {
		super();
		this.userName = userName;
		this.status = status;
		this.email = email;
		this.roleName = roleName;
	}
}
