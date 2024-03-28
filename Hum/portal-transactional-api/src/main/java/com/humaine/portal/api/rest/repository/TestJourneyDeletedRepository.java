package com.humaine.portal.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.TestJourneyDeleted;

@Repository
public interface TestJourneyDeletedRepository extends CrudRepository<TestJourneyDeleted, Long> {

}
