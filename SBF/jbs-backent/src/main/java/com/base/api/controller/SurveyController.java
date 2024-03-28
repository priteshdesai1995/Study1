package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.ADD_QUESTIONS;
import static com.base.api.constants.PermissionConstants.CREATE_SURVEY;
import static com.base.api.constants.PermissionConstants.DELETE_SURVEY;
import static com.base.api.constants.PermissionConstants.GET_ALL_SURVEYS;
import static com.base.api.constants.PermissionConstants.GET_DETAILS;
import static com.base.api.constants.PermissionConstants.GET_QUESTIONS;
import static com.base.api.constants.PermissionConstants.GET_SURVEYS;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_QUESTIONS;
import static com.base.api.constants.PermissionConstants.UPDATE_SURVEYS;
import static com.base.api.constants.PermissionConstants.UPDATE_SURVEY_STATUS;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.entities.SurveyEntity;
import com.base.api.exception.APIException;
import com.base.api.exception.DataNotFoundException;
import com.base.api.exception.QuestionsNotFoundException;
import com.base.api.filter.SurveyFilter;
import com.base.api.repository.ManageSurveyRepository;
import com.base.api.request.dto.QuestionDTO;
import com.base.api.request.dto.SurveyDTO;
import com.base.api.request.dto.SurveyReportDTO;
import com.base.api.service.SurveyService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@Slf4j
@RestController
@RequestMapping("/survey")
public class SurveyController {

	@Autowired
	SurveyService surveyService;

	@Autowired
	ManageSurveyRepository surveyRepository;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * @param surveyFilter
	 * @return This REST API is used to get the list of surveys.
	 */
	@PostMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to Get all the surveys", notes = PERMISSION
			+ GET_ALL_SURVEYS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_SURVEYS })
	public ResponseEntity<TransactionInfo> getAllSurvey(@RequestBody SurveyFilter surveyFilter) {
		log.info("SurveyRestController :: start-getAllSurvey");
		List<SurveyDTO> result = surveyService.getAllSurvey(surveyFilter);
		if (result == null) {
			log.error("no data found");
			throw new APIException("There.is.no.survey.available.here");
		}

		log.info("Result is : " + result.toString());
		return ResponseBuilder.buildOkResponse(result);
	}

	/**
	 * @param surveyFilter
	 * @return This REST API is used to create the survey.
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "API Endpoint to create the surveys", notes = PERMISSION + CREATE_SURVEY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_SURVEY })
	public ResponseEntity<TransactionInfo> createSurvey(@RequestBody SurveyDTO surveyDTO) {
		log.info("SurveyRestController :: start-createSurvey");
		SurveyEntity newSurvey = new SurveyEntity(surveyDTO);
		SurveyEntity createdData = surveyRepository.save(newSurvey);
		log.info("newSurvey is created" + newSurvey.toString());
		if (null != createdData || null != createdData.getId()) {
			log.info("Create Survey successfully");
		} else {
			log.error("not able to create Survey");
			throw new APIException("not.able.to.create.Survey");
		}
		return ResponseBuilder.buildCRUDResponse(createdData, resourceBundle.getString("survey_created"),
				HttpStatus.CREATED);
	}

	/**
	 * @param surveyId
	 * @return This REST API is used to get the survey based on id.
	 */
	@GetMapping(value = "/get_survey")
	@ApiOperation(value = "API Endpoint to get the surveys", notes = PERMISSION + GET_SURVEYS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_SURVEYS })
	public ResponseEntity<TransactionInfo> getSurvey(@RequestParam("survey_id") UUID surveyId) throws Exception {

		log.info("API hit for get the survey based on id");
		Optional<SurveyEntity> result = surveyRepository.findById(surveyId);
		if (!result.isPresent()) {
			throw new APIException("not.able.to.get.survey.based.on.id");
		}
		SurveyEntity surveyData = surveyService.getSurvey(surveyId);
		log.info("Get the survey successfully");
		return ResponseBuilder.buildOkResponse(surveyData);

	}

	@GetMapping(value = "/get_detailed_survey")
	@ApiOperation(value = "API Endpoint to delete the surveys", notes = PERMISSION + GET_DETAILS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_DETAILS })
	public ResponseEntity<TransactionInfo> getSurveyReport(@RequestParam("survey_id") UUID survey_id) {

		SurveyEntity survey = surveyService.getSurvey(survey_id);

		if (survey != null) {
			List<QuestionDTO> questions = surveyService.getQuestions(survey_id);

			SurveyReportDTO report = new SurveyReportDTO();
			report.setSurvey(survey);

			if (!questions.isEmpty())
				report.setQuestions(questions);

			log.info("Report is : " + report.toString());

			return ResponseBuilder.buildOkResponse(report);
		}

		log.error("no survey found");
		throw new DataNotFoundException("survey.id.not.found" + ":" + survey_id);

	}

	/**
	 * @param surveyId
	 * @param surveyFilter
	 * @return This REST API is used to update the survey based on id.
	 */
	@SuppressWarnings("unused")
	@PostMapping(value = "/update")
	@ApiOperation(value = "API Endpoint to update the surveys", notes = PERMISSION + UPDATE_SURVEYS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_SURVEYS })
	public ResponseEntity<TransactionInfo> updateSurvey(@RequestBody SurveyDTO surveyFilter,
			@RequestParam("survey_id") UUID surveyId) throws Exception {
		log.info("API hit of the update the survey");

		Optional<SurveyEntity> data = surveyRepository.findById(surveyId);
		SurveyEntity updateSurvey = data.get();
		if (data == null) {
			log.error("Not able to fetch survey");
			throw new APIException("not.able.to.fetch.survey", HttpStatus.NOT_FOUND);
		}

		updateSurvey.setSurveyTitle(surveyFilter.getTitle());
		updateSurvey.setDescription(surveyFilter.getDescription());
		updateSurvey.setSurveyStatus(surveyFilter.getStatus());
		updateSurvey.setStartDate(surveyFilter.getSurvey_start_date());
		updateSurvey.setEndDate(surveyFilter.getSurvey_end_date());
		updateSurvey.setStatus(surveyFilter.getStatus());

		surveyRepository.save(updateSurvey);

		log.info("Survey data updated successfully..!");
		return ResponseBuilder.buildWithMessage("update.the.survey.successfully");
	}

	/**
	 * @param surveyId
	 * @return This REST API is used to delete the survey based on id
	 */
	@DeleteMapping(value = "/delete")
	@ApiOperation(value = "API Endpoint to delete the surveys", notes = PERMISSION + DELETE_SURVEY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_SURVEY })
	public ResponseEntity<TransactionInfo> deleteSurvey(@RequestParam("survey_id") UUID surveyId) throws Exception {

		log.info("API hit of the delete survey" + surveyId);
		Optional<SurveyEntity> deleteSurvey = surveyRepository.findById(surveyId);
		if (deleteSurvey == null) {
			log.error("no survey found");
			throw new DataNotFoundException("not.able.to.delete.survey" + ":" + surveyId);
		}

		surveyService.deleteSurvey(surveyId);
		log.info("Delete the survey successfully");
		return ResponseBuilder.buildWithMessage("delete.the.survey.successfully");
	}

	/**
	 * @param surveyId
	 * @return This REST API is used to get the questions.
	 */
	@GetMapping(value = "/get_questions")
	@ApiOperation(value = "API Endpoint to get the questions", notes = PERMISSION + GET_QUESTIONS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_QUESTIONS })
	public ResponseEntity<TransactionInfo> getQuestions(@RequestParam("survey_id") UUID surveyId) {
		log.info("SurveyRestController :: start-getQuestions");
		List<QuestionDTO> results = surveyService.getQuestions(surveyId);
		if (results == null) {
			log.error("No questions available in database");
			throw new QuestionsNotFoundException("there.is.no.questions.available.here");
		}
		return ResponseBuilder.buildOkResponse(results);
	}

	/**
	 * @param surveyId
	 * @param questionDTOList
	 * @return This REST API is used to update the survey questions.
	 */
	@PostMapping(value = "/update_questions")
	@ApiOperation(value = "API Endpoint to update the questions", notes = PERMISSION
			+ UPDATE_QUESTIONS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_QUESTIONS })
	public ResponseEntity<TransactionInfo> updateQuestions(@RequestParam("survey_id") UUID surveyId,
			@RequestBody List<QuestionDTO> questionDTOList) {
		log.info("SurveyRestController :: start-updateQuestions");
		String result = surveyService.updateQuestions(surveyId, questionDTOList);
		if (result.equals(HttpStatus.OK.name())) {
			log.info("update the questions successfully in database..!");
			return ResponseBuilder.buildWithMessageAndStatus(result, HttpStatus.OK);
		}
		log.error("something went wrong when I update the questions..!!");

		return ResponseBuilder.buildWithMessageAndStatus(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param surveyId
	 * @param questionDTOList
	 * @return This REST API is used to add questions to survey.
	 */
	@PostMapping(value = "/add_questions")
	@ApiOperation(value = "API Endpoint to add the questions", notes = PERMISSION + ADD_QUESTIONS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ADD_QUESTIONS })
	public ResponseEntity<TransactionInfo> addQuestions(@RequestParam("survey_id") UUID surveyId,
			@RequestBody List<QuestionDTO> questionDTOList) {
		log.info("SurveyRestController :: start-addQuestions");
		String result = surveyService.addQuestions(surveyId, questionDTOList);
		if (result.equals(HttpStatus.OK.name())) {
			log.info("add the questions successfully in database");
			return ResponseBuilder.buildWithMessageAndStatus(result, HttpStatus.OK);
		}
		log.error("while adding the questions getting the issue");
		return ResponseBuilder.buildWithMessageAndStatus(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * @param statusReq
	 * @param surveyId
	 * @return This REST API is used to update the status of survey.
	 */
	@PostMapping(value = "/update_status")
	@ApiOperation(value = "API Endpoint to update the status", notes = PERMISSION
			+ UPDATE_SURVEY_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_SURVEY_STATUS })
	public ResponseEntity<TransactionInfo> changeReportStatus(@RequestBody Map<String, String> statusReq) {

		log.info("SurveyController : changeSurveyStatus");

		log.info("statusReq is : " + statusReq.toString());
		SurveyEntity result = surveyRepository.findById(UUID.fromString(statusReq.get("surveyId"))).get();
		log.info("result is : " + result.toString());
		if (result != null) {

			result.setStatus(statusReq.get("status"));
			surveyRepository.save(result);

			return ResponseBuilder.buildWithMessage("status.changed.successfully");
		}
		log.info("SurveyController : status changed successfully");

		return ResponseBuilder.buildWithMessage("survey.not.found");
	}

}
