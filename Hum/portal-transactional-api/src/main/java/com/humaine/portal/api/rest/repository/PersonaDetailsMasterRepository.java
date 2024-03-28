package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.PersonaDetailsMaster;

@Repository
public interface PersonaDetailsMasterRepository extends CrudRepository<PersonaDetailsMaster, Long> {

	@Query(value = "SELECT pd FROM PersonaDetailsMaster pd WHERE pd.bigFive.id=:bigFiveId AND pd.buy.id=:buyId AND pd.strategies.id=:strategiesId AND pd.values.id=:valuesId")
	PersonaDetailsMaster getPersonaDetailsByIds(@Param("bigFiveId") Long bigFiveId, @Param("buyId") Long buyId,
			@Param("strategiesId") Long strategiesId, @Param("valuesId") Long valuesId);
	
	@Query(value = "SELECT pd FROM PersonaDetailsMaster pd WHERE TRIM(LOWER(pd.bigFive.value))=TRIM(LOWER(:bigFive)) AND TRIM(LOWER(pd.buy.value))=TRIM(LOWER(:buy)) AND TRIM(LOWER(pd.strategies.value))=TRIM(LOWER(:strategies)) AND TRIM(LOWER(pd.values.value))=TRIM(LOWER(:values))")
	PersonaDetailsMaster getPersonaDetailsByValue(@Param("bigFive") String bigFive, @Param("buy") String buy,
			@Param("strategies") String strategies, @Param("values") String values);
}
