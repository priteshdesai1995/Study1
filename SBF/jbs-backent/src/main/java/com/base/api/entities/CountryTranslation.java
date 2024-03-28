package com.base.api.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "country_translable")
@AttributeOverride(name = "id", column = @Column(name = "trans_id"))
public class CountryTranslation extends BaseEntity {

	private static final long serialVersionUID = -1046568134530315587L;

	@Column(name = "locale")
	private String locale;

	@Column(name = "name")
	@NotEmpty(message = "Country name must not be null or empty")
	private String name;

	@Column(name = "uuid")
	@NotEmpty(message = "uuid must not be null or empty")
	private String uuid;
}
