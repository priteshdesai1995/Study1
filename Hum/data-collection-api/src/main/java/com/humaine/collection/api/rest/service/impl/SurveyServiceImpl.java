package com.humaine.collection.api.rest.service.impl;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.GiftCard;
import com.humaine.collection.api.model.SurveyEventRequest;
import com.humaine.collection.api.model.User;
import com.humaine.collection.api.rest.repository.GiftCardRepository;
import com.humaine.collection.api.rest.repository.UserRepository;
import com.humaine.collection.api.rest.service.SurveyService;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;

@Service
public class SurveyServiceImpl implements SurveyService {

	@Autowired
	GiftCardRepository giftCardRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private UserRepository userRepository;
	
	private static final Logger log = LogManager.getLogger(SurveyServiceImpl.class);

	@Override
	public Long startSurvey(SurveyEventRequest surveyEventRequest, Account acc) {
		log.info("startSurvey begins");
		GiftCard giftCard = new GiftCard(DateUtils.getCurrentTimestemp(), UUID.randomUUID().toString(),
				surveyEventRequest.getUserID(), surveyEventRequest.getSessionID(), acc.getId());
		GiftCard card = giftCardRepository.save(giftCard);
		log.info("startSurvey ends");
		return card.getId();
	}

	@Override
	public Long endSurvey(String userID, String sessionId) {
		log.info("endSurvey begins");
		GiftCard giftCard = null;
		giftCard = giftCardRepository.findGiftCardByUserIdAndDate(userID, DateUtils.getCurrentTimestemp());
		if (giftCard != null) {
			giftCard.setSurveyEndTime(DateUtils.getCurrentTimestemp());
			giftCard.setSessionId(sessionId);
		} else {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.giftcard.found", new Object[] {},
					"api.error.giftcard.found.code"));
		}
		giftCard = giftCardRepository.save(giftCard);
		log.info("endSurvey ends");
		return giftCard.getId();
	}

	@Override
	public Boolean checkStatusAPI(String surveyUuid) {
		log.info("checkStatus begins");
		GiftCard giftCard = new GiftCard();
		giftCard = giftCardRepository.findBySurveyUuid(surveyUuid);
		if (giftCard != null) {
			if (giftCard.getSurveyEndTime() != null) {
				return true;
			}
		} else {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.giftcard.found", new Object[] {},
					"api.error.giftcard.found.code"));
		}
		log.info("checkStatus ends");
		return false;
	}

	@Override
	public Account validateRequest(String userID) {
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

		Account account = authentication.getAccount();
		
		User user = this.userRepository.findByUserAndAccountId(userID,
				account.getId());
		
		if (user == null) {
			throw new APIException(errorMessageUtils.getMessageWithCode(
					"api.error.usereventrequest.userID.not.exist", new Object[] { userID },
					"api.error.usereventrequest.userID.not.exist.code"));
		}
		return account;
	}
}
