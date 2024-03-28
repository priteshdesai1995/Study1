package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "persuasive_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Persuasive extends AttributeBaseModel implements Comparable<Persuasive> {
	@Override
	public int compareTo(Persuasive persuasive) {
		return this.getId().compareTo(persuasive.getId());
	}

	public Persuasive(Long id) {
		this.setId(id);
	}
	
	public Persuasive(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
