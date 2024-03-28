package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "configurations")
@AttributeOverride(name = "id", column = @Column(name = "configuration_id"))
public class Configuration extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -7503937206503104472L;

	@Column(name = "option_name", unique = true)
	private String optionName;

	@Column(name = "option_value")
	private String optionValue;

	@Column(name = "option_type")
	private String optionType;

	@Column(name = "type")
	private String type;

	@Column(name = "deleted_at")
	private String deleted_at;

}
