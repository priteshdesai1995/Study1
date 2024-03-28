package com.humaine.portal.api.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "big_five_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BigFive extends AttributeBaseModel {

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "big_five_values", joinColumns = @JoinColumn(name = "big_five_id"), inverseJoinColumns = @JoinColumn(name = "values_id"))
	Set<Values> values = new HashSet<>();

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "big_five_persuasive", joinColumns = @JoinColumn(name = "big_five_id"), inverseJoinColumns = @JoinColumn(name = "persuasive_id"))
	Set<Persuasive> persuasive = new HashSet<>();

	@ManyToMany
	@JsonIgnore
	@JoinTable(name = "big_five_buying", joinColumns = @JoinColumn(name = "big_five_id"), inverseJoinColumns = @JoinColumn(name = "buying_id"))
	Set<Buying> buying = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "big_five_personalities", joinColumns = @JoinColumn(name = "big_five_id"))
	@Column(columnDefinition = "text") 
	@JsonIgnore
	List<String> personalities = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name = "big_five_goals", joinColumns = @JoinColumn(name = "big_five_id"))
	@Column(columnDefinition = "text")
	@JsonIgnore
	List<String> goals = new ArrayList<>();
	
	@ElementCollection
	@CollectionTable(name = "big_five_frustrations", joinColumns = @JoinColumn(name = "big_five_id"))
	@Column(columnDefinition = "text")
	@JsonIgnore
	List<String> frustrations = new ArrayList<>();

	public BigFive(Long id) {
		this.setId(id);
	}

	public BigFive(Long id, String value, OffsetDateTime timestemp, Set<Values> values, Set<Persuasive> persuasive,
			Set<Buying> buying) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
		this.values = values;
		this.persuasive = persuasive;
		this.buying = buying;
	}
}
