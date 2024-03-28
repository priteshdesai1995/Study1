package com.humaine.transactional.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.UserEvent;

@Repository
public interface UserEventRepository extends CrudRepository<UserEvent, Long> {

	@Modifying
	@Query("DELETE FROM UserEvent e WHERE e.timestamp < :date")
	void deleteRecords(@Param("date") OffsetDateTime date);
}
