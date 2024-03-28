package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.api.entities.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {

	@Query(value = "select * from surveyquestions_table q where q.survey_survey_id = :surveyId", nativeQuery = true)
	List<QuestionEntity> findBySurveyId(int surveyId);

	@Query(value = "select Cast(q.question_id as varchar) from surveyquestions_table q where q.survey_survey_id = :surveyId", nativeQuery = true)
	List<String> findQuestionIdBySurveyId(UUID surveyId);

	QuestionEntity[] findBySurveyId(UUID surveyId);

}
