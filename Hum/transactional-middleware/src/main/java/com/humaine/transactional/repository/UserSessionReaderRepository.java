package com.humaine.transactional.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.UserSession;

@Repository
public interface UserSessionReaderRepository extends PagingAndSortingRepository<UserSession, String> {
	@Query("SELECT s FROM UserSession s WHERE s.startTime <= :date")
	Page<UserSession> findArchiveRecords(@Param("date") OffsetDateTime date, Pageable pageable);

	@Query("SELECT count(s) FROM UserSession s WHERE s.startTime <= :date")
	Long getArchiveRecordsCount(@Param("date") OffsetDateTime date);
}
