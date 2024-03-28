package com.base.api.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cities")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class City extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3081637859547173259L;

	@Column(name = "status")
	private String status;

	@Column(name = "uuid", unique = true)
	@NotEmpty(message = "uuid must not be null or empty")
	private String uuid;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "updated_at")
	private Date updatedAt;

	@OneToMany()
	@JoinColumn(name = "city_id")
	private List<CityTranslation> cities;

	@ManyToOne
	@JoinColumn(name = "state_id", nullable = false)
	private State state;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;
}
