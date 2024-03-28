package com.humaine.portal.api.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_name" }) })
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Column(name = "role_name", nullable = false)
	private String roleName;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "create_date")
	private OffsetDateTime createDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Collection<Privilege> privileges = new ArrayList<>();

	public UserRole(Long roleId, String roleName, String status, OffsetDateTime createDate) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.status = status;
		this.createDate = createDate;
	}
}
