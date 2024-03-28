package com.humaine.transactional.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

}
