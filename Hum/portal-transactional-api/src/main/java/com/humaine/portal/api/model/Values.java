package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "values_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Values extends AttributeBaseModel implements Comparable<Values> {
	@Override
	public int compareTo(Values value) {
		return this.getId().compareTo(value.getId());
	}
	public Values(Long id) {
		this.setId(id);
	}
	
	public Values(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
