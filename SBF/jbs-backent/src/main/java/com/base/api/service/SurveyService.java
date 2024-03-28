package com.base.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.base.api.entities.SurveyEntity;
import com.base.api.filter.SurveyFilter;
import com.base.api.request.dto.QuestionDTO;
import com.base.api.request.dto.SurveyDTO;

@Service
public interface SurveyService {

	public void createSurvey(SurveyDTO surveyDTO);
	
	public List<SurveyDTO> getAllSurvey(SurveyFilter surveyFilter);

	public SurveyEntity getSurvey(UUID surveyId);

	public void updateSurvey(UUID surveyId, SurveyDTO surveyDTO) throws Exception;

	public void deleteSurvey(UUID surveyId);
	
	public List<QuestionDTO> getQuestions(UUID surveyId);

	public String updateQuestions(UUID surveyId, List<QuestionDTO> questionDTOList);

	public String addQuestions(UUID surveyId, List<QuestionDTO> questionDTOList);

	//public void changeStatus(UUID surveyId);
	
	public SurveyEntity findBySurveyId(UUID surveyId);
	
}