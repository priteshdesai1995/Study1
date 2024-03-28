package com.base.api.entities.listeners;

import java.time.LocalDateTime;

import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

import com.base.api.entities.BaseEntity;

public class AuditDateTimeListener {

	@PostUpdate
	private void afterUpdate(BaseEntity entity) {
		entity.setUpdatedDate(LocalDateTime.now());
	}

	@PrePersist
	private void beforePersist(BaseEntity entity) {
		entity.setCreatedDate(LocalDateTime.now());
		entity.setUpdatedDate(LocalDateTime.now());
	}
}
