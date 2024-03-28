package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "translatable")
@AttributeOverride(name = "id", column = @Column(name = "translation_id"))
public class Translatable extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -4102834746836143902L;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "locale")
	private String locale;	

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "category_id")
	private Category category;


}
