package com.humaine.portal.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.Gender;



@Repository
public interface GenderRepository extends CrudRepository<Gender, Long> {

	@Query(value = "SELECT g FROM Gender g WHERE TRIM(g.value)!=TRIM(:emptyValue)")
	List<Gender> getGenderList(@Param("emptyValue") String emptyValue);
	
	@Query(value = "SELECT g FROM Gender g WHERE TRIM(g.value)=TRIM(:emptyValue)")
	Gender getGenderEmptyValue(@Param("emptyValue") String emptyValue);
}
