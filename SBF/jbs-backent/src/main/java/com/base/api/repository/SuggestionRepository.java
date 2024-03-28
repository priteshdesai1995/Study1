package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Suggestion;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, UUID> {

	@Query(value = "select * from suggestions s where s.suggestion_id = :id", nativeQuery = true)
	Suggestion findBySuggestionId(UUID id);

}
