package com.humaine.transactional.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.UserEvent;

@Repository
public interface UserEventReaderRepository extends PagingAndSortingRepository<UserEvent, Long> {

	@Query("SELECT e FROM UserEvent e WHERE e.timestamp <= :date")
	Page<UserEvent> findArchiveRecords(@Param("date") OffsetDateTime date, Pageable pageable);

	@Query("SELECT count(e) FROM UserEvent e WHERE e.timestamp <= :date")
	Long getArchiveRecordsCount(@Param("date") OffsetDateTime date);
}
