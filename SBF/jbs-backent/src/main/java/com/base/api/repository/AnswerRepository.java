package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.base.api.entities.AnswerEntity;

public interface AnswerRepository extends JpaRepository<AnswerEntity, UUID> {
	

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "delete from surveyanswers_table a where Cast(a.question_question_id as varchar) in (:questionId)", nativeQuery = true)
	void deleteByQuestionId(List<String> questionId);



}
