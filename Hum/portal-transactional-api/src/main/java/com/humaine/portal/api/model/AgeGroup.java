package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "age_group_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class AgeGroup extends AttributeBaseModel {

	public AgeGroup(Long ageGroupId) {
		this.setId(ageGroupId);
	}

	public AgeGroup(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
