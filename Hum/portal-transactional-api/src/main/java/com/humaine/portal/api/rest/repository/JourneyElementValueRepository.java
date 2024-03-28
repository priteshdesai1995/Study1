package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.JourneyElementValue;

@Repository
public interface JourneyElementValueRepository extends JpaRepository<JourneyElementValue, Long> {

	@Query(value = "select max(u.id) from journey_element_values u", nativeQuery = true)
	Long findJourneyElementMaxId();
}
