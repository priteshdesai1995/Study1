package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.StateTranslation;

@Repository
public interface StateTranslationRepository extends JpaRepository<StateTranslation, UUID> {
	StateTranslation findByUuidAndLocale(String uuid, String locals);
}
