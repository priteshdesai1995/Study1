/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.base.api.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is an entity that represents Role objects in the database.
 * 
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_name" }) })
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
public class UserRole extends BaseEntity {

	private static final long serialVersionUID = 2467460936739245834L;

	@Column(name = "role_name", nullable = false)
	private String roleName;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Collection<Privilege> privileges = new ArrayList<>();

	public UserRole(UUID roleId, String roleName, UserStatus status) {
		super();
		this.id = roleId;
		this.roleName = roleName;
		this.status = status;
	}

	public UserRole(String roleName, UserStatus status) {
		super();
		this.roleName = roleName;
		this.status = status;
	}
}
