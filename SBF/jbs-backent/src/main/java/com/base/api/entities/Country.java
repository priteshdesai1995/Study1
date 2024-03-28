package com.base.api.entities;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
@AttributeOverride(name = "id", column = @Column(name = "country_id"))
public class Country extends BaseEntity {

	private static final long serialVersionUID = 662512661577095219L;

	@Column(name = "status")
	private String status;

	@Column(name = "uuid")
	@NotEmpty(message = "uuid must not be null or empty")
	private String uuid;

	@Column(name = "country_code")
	private String countryCode;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "country_id")
	private List<CountryTranslation> countries;
}
