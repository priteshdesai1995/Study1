package com.base.api.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.base.api.entities.listeners.AuditDateTimeListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditDateTimeListener.class)
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -8102828012564943523L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	protected UUID id;

	@Column(name = "created_date", columnDefinition = "TIMESTAMP")
	protected LocalDateTime createdDate;

	@Column(name = "updated_date", columnDefinition = "TIMESTAMP")
	protected LocalDateTime updatedDate;

}
