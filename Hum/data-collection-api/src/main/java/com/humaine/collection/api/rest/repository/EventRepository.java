package com.humaine.collection.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

}
