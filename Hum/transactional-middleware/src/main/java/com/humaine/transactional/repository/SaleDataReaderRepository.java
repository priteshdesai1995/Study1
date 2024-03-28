package com.humaine.transactional.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humaine.transactional.model.Sale;

@Repository
public interface SaleDataReaderRepository extends PagingAndSortingRepository<Sale, Long> {
	@Query("SELECT s FROM Sale s WHERE s.saleOn <= :date")
	Page<Sale> findArchiveRecords(@Param("date") OffsetDateTime date, Pageable pageable);

	@Query("SELECT count(s) FROM Sale s WHERE s.saleOn <= :date")
	Long getArchiveRecordsCount(@Param("date") OffsetDateTime date);
}
