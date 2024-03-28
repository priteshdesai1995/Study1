package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "family_size_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class FamilySize extends AttributeBaseModel {
	public FamilySize(Long id) {
		this.setId(id);
	}

	public FamilySize(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
