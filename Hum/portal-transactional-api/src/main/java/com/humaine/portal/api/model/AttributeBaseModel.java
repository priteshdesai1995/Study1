package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class AttributeBaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	public Long id;

	@Column(name = "value", unique = true, length = 64)
	public String value;

	@Column(name = "createdon")
	@JsonIgnore
	public OffsetDateTime createdOn;
}
