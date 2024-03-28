package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Persuasive;

@Repository
public interface PersuasiveRepository extends CrudRepository<Persuasive, Long> {

	@Query(value = "SELECT p FROM Persuasive p WHERE TRIM(p.value)=TRIM(:name)")
	Persuasive getAttributeByName(@Param("name") String name);
}
