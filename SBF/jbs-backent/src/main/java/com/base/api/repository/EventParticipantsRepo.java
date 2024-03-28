package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.api.entities.Event;
import com.base.api.entities.EventParticipants;

public interface EventParticipantsRepo extends JpaRepository<EventParticipants, UUID> {
	
	List<EventParticipants> findByEvent(Event eventEntity);
}
