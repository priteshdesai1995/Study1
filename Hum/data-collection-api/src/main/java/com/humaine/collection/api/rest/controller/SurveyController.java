package com.humaine.collection.api.rest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.CouponCode;
import com.humaine.collection.api.model.GiftCard;
import com.humaine.collection.api.model.SurveyEventRequest;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.response.dto.SurveyEligibilityResponse;
import com.humaine.collection.api.response.dto.SurveyStatusResponse;
import com.humaine.collection.api.rest.repository.CouponCodeRepository;
import com.humaine.collection.api.rest.repository.GiftCardRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.rest.repository.UserSessionRepository;
import com.humaine.collection.api.rest.service.SurveyService;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ResponseBuilder;
import com.humaine.collection.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = "Survey", description = "Survey")
@RequestMapping("survey")
public class SurveyController {

	@Autowired
	UserSessionService userSessionService;

	@Autowired
	SurveyService surveyService;

	@Autowired
	private CouponCodeRepository couponCodeRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private GiftCardRepository giftCardRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Value(value = "${daily.giftcard.limit}")
	String dailyGiftCardLimit;

	@ApiOperation(value = "start survey for user", notes = "start survey for user", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@PostMapping(path = "", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<TransactionInfo> startSurvey(@RequestBody SurveyEventRequest surveyEventRequest)
			throws APIException {

		Account account = surveyService.validateRequest(surveyEventRequest.getUserID());

		GiftCard giftCard = new GiftCard();
		giftCard = giftCardRepository.findGiftCardByUserId(surveyEventRequest.getUserID());

		if (giftCard != null && giftCard.getSurveyEndTime() != null) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.giftcard.response.submitted",
					new Object[] {}, "api.error.giftcard.response.submitted"));
		}
		if (giftCard != null && giftCard.getSurveyEndTime() == null) {
			return ResponseBuilder
					.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode("api.success.start-survey.create",
							new Object[] { giftCard.getId() }, "api.success.start-survey.create.code"));
		}

		Long surveyUuid = surveyService.startSurvey(surveyEventRequest, account);
		return ResponseBuilder
				.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode("api.success.start-survey.create",
						new Object[] { surveyUuid }, "api.success.start-survey.create.code"));
	}

	@ApiOperation(value = "end survey for user", notes = "end survey for user", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@PostMapping(path = "/endSurvey", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<TransactionInfo> endSurvey(@RequestBody SurveyEventRequest surveyEventRequest)
			throws APIException {
		System.out.println(surveyEventRequest);
		surveyService.validateRequest(surveyEventRequest.getUserID());

		GiftCard giftCard = null;
		giftCard = giftCardRepository.findGiftCardByUserId(surveyEventRequest.getUserID());

		if (giftCard == null) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.giftcard.found", new Object[] {},
					"api.error.giftcard.found.code"));
		}
		Optional<UserSession> userSession = this.userSessionRepository.findById(surveyEventRequest.getSessionID());
		if (userSession.isEmpty()) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.session.notfound",
					new Object[] { surveyEventRequest.getSessionID() }, "api.error.session.notfound.code"));
		}
		surveyService.endSurvey(surveyEventRequest.getUserID(), surveyEventRequest.getSessionID());
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.end-survey.create", null, "api.success.end-survey.create.code"));
	}

	@ApiOperation(value = "check status for user", notes = "check status for user", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@GetMapping(value = "/checkStatus/{userId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<TransactionInfo> checkStatusAPI(@PathVariable(name = "userId") String userId)
			throws APIException {
		try {
			surveyService.validateRequest(userId);
		} catch (Exception e) {
			new SurveyStatusResponse(false, null);
		}
		GiftCard giftCard = null;
		giftCard = giftCardRepository.findGiftCardByUserId(userId);

		if (giftCard == null) {
			return ResponseBuilder.buildResponse(new SurveyStatusResponse(false, null));
		}

		if (giftCard.getSurveyEndTime() == null) {
			return ResponseBuilder.buildResponse(new SurveyStatusResponse(false, null));
		}
		CouponCode couponCodeData = new CouponCode();
		couponCodeData = couponCodeRepository
				.findCouponCodeByCurrentDate(DateUtils.getCurrentTimestemp(DateUtils.AMERICA_LOS_ANGELES_ZONE_ID).toLocalDate());
		return ResponseBuilder.buildResponse(new SurveyStatusResponse(true, couponCodeData.getCouponCode()));
	}

	@ApiOperation(value = "check user is Allow For GiftCard", notes = "check status for user", authorizations = {
			@Authorization(value = CustomAuthenticationFilter.HEADER) })
	@GetMapping(value = "/check-eligibility/{userId}", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<TransactionInfo> checkEligibilityAPI(@PathVariable(name = "userId") String userId)
			throws APIException {

		Account account = surveyService.validateRequest(userId);

		Long count = giftCardRepository.findGiftCardCount();

		if (count >= getDailyLimit()) {
			return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(false, false));
		}
		CouponCode couponCodeData = couponCodeRepository
				.findCouponCodeByCurrentDate(DateUtils.getCurrentTimestemp(DateUtils.AMERICA_LOS_ANGELES_ZONE_ID).toLocalDate());
		if (couponCodeData == null) {
			return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(false, false));
		}
		GiftCard giftCard = null;
		giftCard = giftCardRepository.findGiftCardByUserId(userId);

		if (giftCard != null && giftCard.getSurveyEndTime() != null) {
			return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(false, false));
		}

		if (giftCard != null) {
			return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(false, true));
		}

//		User u = this.userRepository.findByUserAndAccountId(userId, account.getId());
//
//		if (u != null && DateUtils.isSameDay(u.getCreatedOn(), DateUtils.getCurrentTimestemp())) {
//			return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(true, true));
//		}
//		return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(false, false));
		return ResponseBuilder.buildResponse(new SurveyEligibilityResponse(true, true));
	}

	Long getDailyLimit() {
		String limit = dailyGiftCardLimit;
//		if (limit == null) {
//			limit = "50";
//		}
//		limit = limit.trim();
		return Long.valueOf(limit);
	}
}