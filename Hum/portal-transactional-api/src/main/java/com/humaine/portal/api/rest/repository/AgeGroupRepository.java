package com.humaine.portal.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.AgeGroup;

@Repository
public interface AgeGroupRepository extends CrudRepository<AgeGroup, Long> {

}
