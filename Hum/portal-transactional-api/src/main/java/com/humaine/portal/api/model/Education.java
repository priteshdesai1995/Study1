package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "education_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Education extends AttributeBaseModel {

	public Education(Long id) {
		this.setId(id);
	}
	
	public Education(Long id, String value, OffsetDateTime timestemp) {
		this.setId(id);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
