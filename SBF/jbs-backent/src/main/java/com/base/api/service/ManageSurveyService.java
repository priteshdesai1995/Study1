package com.base.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.base.api.entities.ManageSurveyEntity;
import com.base.api.filter.SurveyFilter;
import com.base.api.request.dto.QuestionDTO;
import com.base.api.request.dto.SurveyDTO;

@Service
public interface ManageSurveyService {

	
	public List<SurveyDTO> getAllSurvey(SurveyFilter surveyFilter);

	public String createSurvey(SurveyFilter surveyFilter);

	public List<QuestionDTO> getQuestions(UUID surveyId);

	public String updateQuestions(UUID surveyId, List<QuestionDTO> questionDTOList);

	public String addQuestions(UUID surveyId, List<QuestionDTO> questionDTOList);
	
	public ManageSurveyEntity getSurvey(UUID surveyId);

	public String updateSurvey(UUID surveyId, SurveyFilter surveyFilter);

	public String deleteSurvey(UUID surveyId);

}
