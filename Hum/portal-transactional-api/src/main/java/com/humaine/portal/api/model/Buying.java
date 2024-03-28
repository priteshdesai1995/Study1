package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "buying_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Buying extends AttributeBaseModel implements Comparable<Buying> {
	@Override
	public int compareTo(Buying buying) {
		return this.getId().compareTo(buying.getId());
	}

	public Buying(Long id) {
		this.setId(id);
	}

	public Buying(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
