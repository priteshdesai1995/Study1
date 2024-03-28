package com.base.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Country;
import com.base.api.entities.CountryTranslation;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {

	@Query("Select co from Country c join c.countries co where co.locale = :locale and c.status = :status")
	List<CountryTranslation> findByCountriesLocaleAndStatus(String locale, String status);

	@Query("Select c from Country c where c.countryCode = :countryCode")
	Optional<Country> findByName(String countryCode);

	Country findByCountries_Uuid(String uuid);

	Optional<Country> findByUuid(String uuid);
	
}
