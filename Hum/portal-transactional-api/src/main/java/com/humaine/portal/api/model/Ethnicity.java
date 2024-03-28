package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ethnicity_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Ethnicity extends AttributeBaseModel {
	public Ethnicity(Long id) {
		this.setId(id);
	}

	public Ethnicity(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
