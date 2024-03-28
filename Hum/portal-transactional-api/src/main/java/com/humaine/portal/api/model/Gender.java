package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "gender_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Gender extends AttributeBaseModel {

	@Column(name = "is_other", columnDefinition = "boolean default true")
	private Boolean isOther;

	public Gender(Long genderId) {
		this.setId(genderId);
	}
	
	public Gender(Long genderId, String value, OffsetDateTime timestemp) {
		this.setId(genderId);
		this.setValue(value);
		this.setCreatedOn(timestemp);
	}
}
