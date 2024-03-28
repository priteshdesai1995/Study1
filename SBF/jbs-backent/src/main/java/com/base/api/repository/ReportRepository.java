package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Report;

/**
 * @author preyansh_prajapati
 *
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID>{

	
}
