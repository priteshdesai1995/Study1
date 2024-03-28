package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.CityTranslation;

@Repository
public interface CityTranslableRepository extends JpaRepository<CityTranslation, UUID> {
	
	CityTranslation findByUuid(String uuid);
	
	CityTranslation findByUuidAndLocale(String uuid, String locale);
}
