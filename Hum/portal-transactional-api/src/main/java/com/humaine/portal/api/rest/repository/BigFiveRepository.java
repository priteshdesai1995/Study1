package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.BigFive;

@Repository
public interface BigFiveRepository extends CrudRepository<BigFive, Long> {

	@Query(value = "SELECT b.id FROM BigFive b WHERE TRIM(LOWER(b.value))=TRIM(LOWER(:bigFive))")
	BigFive getBigFiveIdByName(@Param("bigFive") String bigFive);
}
