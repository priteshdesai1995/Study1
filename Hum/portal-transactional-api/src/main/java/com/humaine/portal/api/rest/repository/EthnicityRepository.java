package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Ethnicity;

@Repository
public interface EthnicityRepository extends CrudRepository<Ethnicity, Long> {

	@Query(value = "SELECT e FROM Ethnicity e WHERE TRIM(e.value)!=TRIM(:emptyValue)")
	List<Ethnicity> getEthnicityList(@Param("emptyValue") String emptyValue);
	
	@Query(value = "SELECT e FROM Ethnicity e WHERE TRIM(e.value) = TRIM(:emptyValue)")
	Ethnicity getEthnicityEmptyValue(@Param("emptyValue") String emptyValue);
}
