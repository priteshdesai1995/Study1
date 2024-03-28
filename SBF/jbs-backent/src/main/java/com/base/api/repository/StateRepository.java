package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.api.entities.State;
import com.base.api.entities.StateTranslation;

@Repository
public interface StateRepository extends JpaRepository<State, UUID> {

	@Query("Select so from State s join s.states so join s.country co  where so.locale = 'en' and s.status = 'Active' and co.id = :countryId")
	List<StateTranslation> findByCountryId(@Param("countryId") UUID countryId);

	List<State> findByCountry_Id(UUID id);
	
	State findByUuid(String uuid);
}
