package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Buying;

@Repository
public interface BuyingRepository extends CrudRepository<Buying, Long> {

	@Query(value = "SELECT b FROM Buying b WHERE TRIM(b.value)=TRIM(:name)")
	Buying getAttributeByName(@Param("name") String name);
}
