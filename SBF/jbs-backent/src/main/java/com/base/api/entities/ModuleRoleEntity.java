package com.base.api.entities;


import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "module_roles")
@AttributeOverride(name = "id", column = @Column(name = "module_role_id"))
public class ModuleRoleEntity extends BaseEntity {

	private static final long serialVersionUID = -7363475636314555706L;

	@OneToOne
	private ModuleEntity module;

	@OneToOne
	private UserRole role;

}
