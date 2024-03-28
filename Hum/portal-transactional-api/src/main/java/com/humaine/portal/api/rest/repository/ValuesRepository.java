package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Values;

@Repository
public interface ValuesRepository extends CrudRepository<Values, Long> {

	@Query(value = "SELECT v FROM Values v WHERE TRIM(v.value)=TRIM(:name)")
	Values getAttributeByName(@Param("name") String name);
}
