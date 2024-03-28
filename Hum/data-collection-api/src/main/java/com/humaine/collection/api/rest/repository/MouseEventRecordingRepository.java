package com.humaine.collection.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.collection.api.model.EventData;

@Repository
public interface MouseEventRecordingRepository extends JpaRepository<EventData, Long> {

}
