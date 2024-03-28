package com.base.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.api.entities.AnswerEntity;
import com.base.api.entities.QuestionEntity;
import com.base.api.entities.SurveyEntity;
import com.base.api.exception.APIException;
import com.base.api.exception.DataNotFoundException;
import com.base.api.exception.QuestionsNotFoundException;
import com.base.api.filter.FilterBase;
import com.base.api.filter.SurveyFilter;
import com.base.api.repository.AnswerRepository;
import com.base.api.repository.ManageSurveyRepository;
import com.base.api.repository.QuestionRepository;
import com.base.api.request.dto.QuestionDTO;
import com.base.api.request.dto.SurveyDTO;
import com.base.api.service.SurveyService;
import com.base.api.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	EntityManager entityManager;

	@Autowired
	ManageSurveyRepository surveyRepository;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	AnswerRepository answerRepository;

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyDTO> getAllSurvey(SurveyFilter filter) {

		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query.append(" u.status LIKE '%" + filter.getStatus() + "%' ");
		}
		if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.surveyTitle LIKE '%" + filter.getTitle() + "%'");
		}
		if (filter.getSurvey_status() != null && !filter.getSurvey_status().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" u.surveyStatus LIKE '%" + filter.getSurvey_status() + "%'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());

		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<SurveyEntity> results = new ArrayList<SurveyEntity>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		log.info("Result is : " + results.toString());
		List<SurveyDTO> list = new ArrayList<SurveyDTO>();
		for (SurveyEntity suggestionEntity : results) {
			SurveyDTO suggestionDTO = new SurveyDTO();
			suggestionDTO = this.mapEntityToDTO(suggestionEntity);
			list.add(suggestionDTO);
		}

		return list;
	}

	private SurveyDTO mapEntityToDTO(SurveyEntity entity) {
		SurveyDTO surveyDTO = new SurveyDTO();
		log.info("entity is : " + entity.toString());
		surveyDTO.setSurveyId(entity.getId());
		surveyDTO.setTitle(entity.getSurveyTitle());
		surveyDTO.setSurvey_start_date(entity.getStartDate());
		surveyDTO.setSurvey_end_date(entity.getEndDate());
		surveyDTO.setSurvey_status(entity.getSurveyStatus());
		surveyDTO.setStatus(entity.getStatus());

		return surveyDTO;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from SurveyEntity u where");
		return query;
	}

	@Transactional
	@Override
	public void createSurvey(SurveyDTO surveyDTO) {

		log.info("SurveyServiceImpl : create-or-save-survey");

		SurveyEntity createSurvey = new SurveyEntity(surveyDTO);
		if (null != createSurvey.getId()) {
			surveyRepository.save(createSurvey);
			log.info("create survey successfully...!");
		} else {
			log.error("some thing went wrong while creating survey");
			throw new APIException("failed.to.create.survey");
		}

	}

	@Override
	public SurveyEntity getSurvey(UUID surveyId) {

		log.info("SurveyServiceImpl : Get-Data-ById");
		SurveyEntity surveyData = surveyRepository.getById(surveyId);

		if (null != surveyData) {
			log.info("Getting the Data based on Id..!");
			return surveyData;
		} else {
			log.error("No such data is found by id :" + surveyData);
			throw new DataNotFoundException("survey.id.not.found");
		}
	}

	@Transactional
	@Override
	public void updateSurvey(UUID surveyId, SurveyDTO surveyDTO) throws Exception {

		log.info("SurveyServiceImpl : UpdateSurveyData");

		Optional<SurveyEntity> dataOptional = surveyRepository.findById(surveyId);
		if (dataOptional.isPresent() && null != dataOptional.get()) {
			SurveyEntity survey = dataOptional.get();
			surveyRepository.save(survey);
			log.info("survey updated successfully...");
		} else {
			throw new DataNotFoundException("invalid.id.was.provided..!");
		}

	}

	@Override
	public void deleteSurvey(UUID surveyId) {

		Optional<SurveyEntity> surveyEntity = surveyRepository.findById(surveyId);
		log.info("surveyEntity is : " + surveyEntity.toString());
		if (surveyEntity == null) {
			throw new QuestionsNotFoundException("There.is.no.survey.id");
		}
		List<String> questionIds = questionRepository.findQuestionIdBySurveyId(surveyId);
		log.info("questionIds is : " + questionIds.toString());
		if (!questionIds.isEmpty()) {
			answerRepository.deleteByQuestionId(questionIds);
		}
		QuestionEntity[] questionEntity = questionRepository.findBySurveyId(surveyId);
		log.info("questionEntity is : " + Arrays.toString(questionEntity));
		if (questionEntity != null) {
			for (QuestionEntity questionEntityId : questionEntity) {
				questionRepository.delete(questionEntityId);
			}
		}

		surveyRepository.deleteById(surveyId);
		log.info("survey deleted successfully");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuestionDTO> getQuestions(UUID surveyId) {
//		======> SQL Native Query	
//		String query = "select q.*, a.* from surveyquestions_table as q inner join surveyanswers_table as a on Cast(q.question_id as varchar) = Cast(a.question_question_id as varchar) and Cast(q.survey_survey_id as varchar) = '" + surveyId + "' order by Cast(q.question_id as varchar)";
		String query = "select b from AnswerEntity b join b.question q join q.survey p where p.id = '" + surveyId
				+ "' order by p.id";

		List<QuestionDTO> result = new ArrayList<QuestionDTO>();

		List<AnswerEntity> answerEntityList = entityManager.createQuery(query).getResultList();

		if (answerEntityList.size() < 1) {
			return result;
		}

		QuestionEntity temp = null;
		QuestionDTO questionDTO = null;

		List<AnswerEntity> answerList = null;

		for (AnswerEntity answerEntity : answerEntityList) {

			if (temp == null || !temp.equals(answerEntity.getQuestion())) {

				if (questionDTO != null && answerList != null) {
					questionDTO.setAnswerType(answerList.get(0).getAnswerType());
					questionDTO.setQuestion(temp);
					questionDTO.setAnswers(answerList);
					result.add(questionDTO);
				}

				questionDTO = new QuestionDTO();
				answerList = new ArrayList<AnswerEntity>();

				temp = answerEntity.getQuestion();
			}
			answerList.add(answerEntity);
		}

		if (questionDTO != null && answerList != null) {
			questionDTO.setAnswerType(answerList.get(0).getAnswerType());
			questionDTO.setQuestion(temp);
			questionDTO.setAnswers(answerList);
			result.add(questionDTO);
		}

		return result;
	}

	@Transactional
	@Override
	public String updateQuestions(UUID surveyId, List<QuestionDTO> questionDTOList) {
		List<String> questionIds = questionRepository.findQuestionIdBySurveyId(surveyId);
		answerRepository.deleteByQuestionId(questionIds);
		for (QuestionEntity questionEntity : questionRepository.findBySurveyId(surveyId)) {
			questionRepository.delete(questionEntity);
		}

		for (QuestionDTO questionDTO : questionDTOList) {

			// questionDTO.getQuestion().setQuestionId(null);
			QuestionEntity questionEntity = questionRepository.save(questionDTO.getQuestion());

			for (AnswerEntity answerEntity : questionDTO.getAnswers()) {
				// answerEntity.setAnswerId(null);
				answerEntity.setAnswerType(questionDTO.getAnswerType());
				answerEntity.setQuestion(questionEntity);
				answerRepository.save(answerEntity);
			}

		}

		return HttpStatus.OK.name();
	}

	@Override
	public String addQuestions(UUID surveyId, List<QuestionDTO> questionDTOList) {
		SurveyEntity survey = getSurvey(surveyId);
		log.info("survey is : " + survey.toString());
		if (survey != null) {

			log.info("questionDTOList is : " + questionDTOList.toString());
			for (QuestionDTO questionDTO : questionDTOList) {

				questionDTO.getQuestion().setSurvey(survey);
				QuestionEntity questionEntity = questionRepository.save(questionDTO.getQuestion());

				for (AnswerEntity answerEntity : questionDTO.getAnswers()) {
					answerEntity.setAnswerType(questionDTO.getAnswerType());
					answerEntity.setQuestion(questionEntity);
					answerRepository.save(answerEntity);
				}

			}
		}

		return HttpStatus.OK.name();
	}

//	@Override
//	public void changeStatus(UUID surveyId) {
//		log.info("SurveyServiceImpl : change status");
//
//		SurveyEntity survey = getSurvey(surveyId);
//
//		boolean oldStatus = survey.isStatus();
//		survey.setStatus(!oldStatus);
//
//		surveyRepository.save(survey);
//
//		log.info("SurveyServiceImpl : survey status changed successfully");
//
//	}

	@Override
	public SurveyEntity findBySurveyId(UUID surveyId) {
		SurveyEntity survey = getSurvey(surveyId);
		return survey;
	}

}
