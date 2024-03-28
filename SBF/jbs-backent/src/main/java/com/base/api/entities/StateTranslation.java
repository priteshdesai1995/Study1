package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "state_translable")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class StateTranslation extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 4811219310305392639L;

	@Column(name = "locale")
	private String locale;

	@Column(name = "name")
	@NotEmpty(message = "State name must not be null or empty")
	private String name;

	@Column(name = "uuid")
	@NotEmpty(message = "uuid must not be null or empty")
	private String uuid;

}
