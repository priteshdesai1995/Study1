package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.SurveyEntity;

@Repository
public interface ManageSurveyRepository extends JpaRepository<SurveyEntity,UUID>{
	
}
