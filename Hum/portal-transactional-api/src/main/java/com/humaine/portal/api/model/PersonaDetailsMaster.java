package com.humaine.portal.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "persona_details_master")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PersonaDetailsMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	public Long id;

	@ManyToOne
	@JoinColumn(name = "big_five")
	BigFive bigFive;

	@ManyToOne
	@JoinColumn(name = "buy")
	Buying buy;

	@ManyToOne
	@JoinColumn(name = "strategies")
	Persuasive strategies;

	@ManyToOne
	@JoinColumn(name = "values")
	Values values;

	@ElementCollection
	@CollectionTable(name = "persona_goals", joinColumns = @JoinColumn(name = "persona_details_id"))
	@Column(columnDefinition = "text")
	@JsonIgnore
	List<String> goals = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "persona_frustrations", joinColumns = @JoinColumn(name = "persona_details_id"))
	@Column(columnDefinition = "text")
	@JsonIgnore
	List<String> frustrations = new ArrayList<>();

	@ElementCollection
	@CollectionTable(name = "persona_personality", joinColumns = @JoinColumn(name = "persona_details_id"))
	@Column(columnDefinition = "text")
	@JsonIgnore
	List<String> personalities = new ArrayList<>();
}
