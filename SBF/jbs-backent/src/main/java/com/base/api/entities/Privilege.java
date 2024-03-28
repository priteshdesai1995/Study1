/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This class is an entity that represents Privilege objects in the database.
 * 
 * @author minesh_prajapati
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString
@Entity
@Table(name = "permissions")
@AttributeOverride(name = "id", column = @Column(name = "permission_id"))
public class Privilege extends BaseEntity {

	private static final long serialVersionUID = 1424116015858980838L;

	@Column(name = "permission", unique = true)
	private String authority;

	public Privilege(String authority) {
		this.authority = authority;
	}
}
