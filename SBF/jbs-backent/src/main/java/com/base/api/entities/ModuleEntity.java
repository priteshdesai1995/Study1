package com.base.api.entities;


import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "modules")
@AttributeOverride(name = "id", column = @Column(name = "module_id"))
public class ModuleEntity extends BaseEntity{

	private static final long serialVersionUID = -2486535101072993660L;

	@Column(name = "module_type")
//	@ColumnDefault("folder")
	private String type;

	@Column(name = "module_text")
	private String text;

	@Column(name = "permission_key")
//	@ColumnDefault("")
	private String permissionKey;

	@JsonProperty("parent")
	@ManyToOne()
	private ModuleEntity parent;


}
