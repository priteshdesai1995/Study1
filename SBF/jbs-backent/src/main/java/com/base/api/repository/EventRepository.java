package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

}