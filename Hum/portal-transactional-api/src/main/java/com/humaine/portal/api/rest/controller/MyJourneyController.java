package com.humaine.portal.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.MyJourneyDeleted;
import com.humaine.portal.api.olap.model.OLAPMyJourney;
import com.humaine.portal.api.request.dto.MultipleDeleteRequests;
import com.humaine.portal.api.response.dto.MyJourneyAnalysisResponse;
import com.humaine.portal.api.response.dto.MyJourneyDBResponse;
import com.humaine.portal.api.rest.olap.repository.MyJourneyRepository;
import com.humaine.portal.api.rest.olap.service.OLAPMyJourneyService;
import com.humaine.portal.api.rest.repository.JourneyElementMasterRepository;
import com.humaine.portal.api.rest.repository.MyJourneyDeletedRepository;
import com.humaine.portal.api.rest.repository.UserGroupRepository;
import com.humaine.portal.api.rest.service.AuthService;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "My Journey", description = "My Journey")
@RequestMapping("/my-journey")
public class MyJourneyController {

	@Autowired
	AuthService authService;

	@Autowired
	MyJourneyRepository myJourneyRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	OLAPMyJourneyService journeyService;

	@Autowired
	JourneyElementMasterRepository journeyElementMasterRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	MyJourneyDeletedRepository myJourneyDeletedRepository;

	private static final Logger log = LogManager.getLogger(MyJourneyController.class);

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get My Journey List", notes = "Get My Journey List", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getMyJourneyList(HttpServletRequest httpRequest) {
		log.info("Get My Journey List: start");
		Account account = authService.getLoginUserAccount(true, httpRequest);
		Long totalSteps = journeyElementMasterRepository.count();
		List<MyJourneyDBResponse> journeyList = myJourneyRepository.getMyJourneyList(account.getId());
		List<MyJourneyAnalysisResponse> journeyAnalysis = new ArrayList<MyJourneyAnalysisResponse>();
		for (int i = 0; i < journeyList.size(); i++) {
			MyJourneyAnalysisResponse journey = new MyJourneyAnalysisResponse(journeyList.get(i));
			journey.setTotalJourneySteps(totalSteps);
			journeyAnalysis.add(journey);
		}
		journeyService.fillGroupNameAndBigFiveForList(journeyAnalysis);
		log.info("Get My Journey List: end");
		return ResponseBuilder.buildResponse(journeyAnalysis);
	}

	@DeleteMapping(value = "{journeyId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete My Journey", notes = "Delete My Journey", authorizations = {
			@Authorization(value = AWSConfig.header) })
	ResponseEntity<TransactionInfo> deleteTestJourney(@PathVariable(name = "journeyId") Long journeyId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Delete My Journey By Id: Journey Id({}) : AccountId: ({}): start", journeyId, account.getId());
		OLAPMyJourney journey = myJourneyRepository.getJourneyById(account.getId(), journeyId);
		if (journey == null) {
			log.error("No Journey Found With Id: {}", journeyId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.journey.not.found",
					new Object[] { journeyId }, "api.error.journey.not.found.code"));
		}

		MyJourneyDeleted deleted = new MyJourneyDeleted(journey, DateUtils.getCurrentTimestemp());
		myJourneyDeletedRepository.save(deleted);
		myJourneyRepository.delete(journey);
		log.info("Delete Test Journey By Id: Journey Id({}) : AccountId: ({}): end", journeyId, account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.deleted", null, "api.success.ai.journey.deleted.code"));
	}

	@DeleteMapping(headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete Multiple My Journeys By Ids", notes = "Delete Multiple My Journeys By Ids", authorizations = {
			@Authorization(value = AWSConfig.header) })
	ResponseEntity<TransactionInfo> deleteTestJourney(@RequestBody MultipleDeleteRequests<Long> request, HttpServletRequest httpRequest) {
		if (request == null)
			request = new MultipleDeleteRequests<>();
		if (request.getIds() == null)
			request.setIds(new ArrayList<Long>());
		if (request.getIds().isEmpty()) {
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.usergroup.delete.ids.empty",
					new Object[] {}, "api.error.usergroup.delete.ids.empty"));
		}
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Delete Test Journey By Ids: Journy Ids({}) : AccountId: ({}): start", request.getIds(),
				account.getId());
		List<OLAPMyJourney> journey = myJourneyRepository.getJourneyByIds(account.getId(), request.getIds());
		journey.stream().forEach(e -> {
			MyJourneyDeleted deleted = new MyJourneyDeleted(e, DateUtils.getCurrentTimestemp());
			myJourneyDeletedRepository.save(deleted);
			myJourneyRepository.delete(e);
		});
		log.info("Delete Test Journey By Ids: Journy Ids({}) : AccountId: ({}): end", request.getIds(),
				account.getId());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.ai.journey.deleted", null, "api.success.ai.journey.deleted.code"));
	}

	@GetMapping(value = "{journeyId}", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Journey Details By Id", notes = "Get Journey Details By Id", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getJourneyDetails(@PathVariable(name = "journeyId") Long journeyId, HttpServletRequest httpRequest) {
		Account account = authService.getLoginUserAccount(true, httpRequest);
		log.info("Get Test Journey Details By Id: Journy Id({}) : AccountId: ({}): start", journeyId, account.getId());
		Long totalSteps = journeyElementMasterRepository.count();
		MyJourneyDBResponse journey = myJourneyRepository.findJourneyById(account.getId(), journeyId);
		if (journey == null) {
			log.error("No Journey Found With Id: {}", journeyId);
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.journey.not.found",
					new Object[] { journeyId }, "api.error.journey.not.found.code"));
		}
		MyJourneyAnalysisResponse response = new MyJourneyAnalysisResponse(journey);
		response.setTotalJourneySteps(totalSteps);
		journeyService.fillGroupNameAndBigFive(response);
		log.info("Get Test Journey Details By Id: Journy Id({}) : AccountId: ({}): end", journeyId, account.getId());
		return ResponseBuilder.buildResponse(response);
	}
}
