package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.CountryTranslation;

@Repository
public interface CountryTranslationRepository extends JpaRepository<CountryTranslation, UUID> {
	
	List<CountryTranslation> findByLocale(String locale);
	
	CountryTranslation findByUuid(String uuid);

}