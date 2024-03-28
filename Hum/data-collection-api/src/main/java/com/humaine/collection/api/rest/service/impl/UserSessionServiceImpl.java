package com.humaine.collection.api.rest.service.impl;

import java.time.OffsetDateTime;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.humaine.collection.api.enums.UserEvents;
import com.humaine.collection.api.es.projection.model.EnddedSession;
import com.humaine.collection.api.es.repository.impl.ESUserEventRepository;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.UserEvent;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.rest.repository.UserEventRepository;
import com.humaine.collection.api.rest.repository.UserSessionRepository;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;

@Service
public class UserSessionServiceImpl implements UserSessionService {

	private static final Logger log = LogManager.getLogger(UserSessionServiceImpl.class);

	private final int closeOffset = 10;

	@Autowired
	private UserSessionRepository userSessionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private UserEventRepository userEventRepository;

	@Autowired
	private ESUserEventRepository esUserEventRepository;

	@Override
	public UserSession startUsersession(UserEventRequest userEventRequest, OffsetDateTime timestemp)
			throws APIException {
		log.debug("In side Start User Session");
		if (timestemp == null)
			timestemp = DateUtils.getCurrentTimestemp();
		this.validateUserEventRequest(userEventRequest);
		boolean isSessionExist = this.checkSessionExist(userEventRequest.getSessionID());
		if (isSessionExist) {
			log.error("Session Already Started with SessionID: {}", userEventRequest.getSessionID());
			this.checkIsSessionEnd(userEventRequest.getSessionID());
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.session.already.start",
					new Object[] { userEventRequest.getSessionID() }, "api.error.session.already.start.code"));
		}
		UserSession userSessionEntity = new UserSession(userEventRequest, timestemp);
		this.userSessionRepository.save(userSessionEntity);
		log.info("User Session Info Saved: {}", userSessionEntity.toString());
		return userSessionEntity;
	}

	@Override
	public UserSession endUsersession(UserEventRequest userEventRequest, OffsetDateTime timestemp) throws APIException {
		log.debug("In side End User Session");
		if (timestemp == null)
			timestemp = DateUtils.getCurrentTimestemp();
		this.validateUserEventRequest(userEventRequest);
		UserSession userSession = this.userSessionRepository.getByIdAndAccountId(userEventRequest.getSessionID(),userEventRequest.getAccountID());
		if (userSession == null) {
			log.error("Session not Found with SessionID: {}", userEventRequest.getSessionID());
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.session.notfound",
					new Object[] { userEventRequest.getSessionID() }, "api.error.session.notfound.code"));
		}
		UserSession userSessionEntity = userSession;
		userSessionEntity.setEndTime(timestemp);
		this.userSessionRepository.save(userSessionEntity);
		log.info("User Session Info Saved: {}", userSessionEntity.toString());
		log.info("DHAVAL ... User Session ENDED -------------- ", userSessionEntity.toString());
		return userSessionEntity;
	}

	@Override
	public boolean checkSessionExist(String session) {
		log.debug("In side get User Session by Id");
		if (StringUtils.isBlank(session)) {
			log.error("Session id is Empty or null");
			return false;
		}
		Optional<UserSession> userSession = this.userSessionRepository.findById(session);
		return !userSession.isEmpty();
	}

	@Override
	public UserSession validateUserSessionValidation(String session, UserEvents event) {
		if (UserEvents.START.equals(event))
			return null;
		if (StringUtils.isBlank(session)) {
			log.error("Session id is Empty or null");
			return null;
		}
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		Account account = authentication.getAccount();
		UserSession userSession = this.userSessionRepository.getByIdAndAccountId(session, account.getId());
		if (userSession == null) {
			return null;
		}
		UserSession sess = userSession;
		if (sess.getEndTime() == null) {
			Long diff = DateUtils.getDuration(userSession.getStartTime());
			if (diff >= account.getSessionTimeout()) {
				sess.setEndTime(sess.getStartTime().plusSeconds(account.getSessionTimeout()));
				this.userSessionRepository.save(sess);
				return sess;
			}
		}
		return null;
	}

	@Override
	public void validateSession(String session) {
		log.debug("In side Validate User Session");
		boolean isExist = this.checkSessionExist(session);

		if (!isExist) {
			log.error("Session Not found with Session Id: {}", session);
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.session.notfound",
					new Object[] { session }, "api.error.session.notfound.code"));
		}

		this.checkIsSessionEnd(session);
	}

	private void validateUserEventRequest(UserEventRequest userEventRequest) {
		log.debug("In side Validate validateUserEventRequest");
		if (userEventRequest.getAccountID() == null) {
			log.error("Account id is Empty or null");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.accountID.null", null,
					"api.error.accountID.null.code"));
		}

		if (StringUtils.isBlank(userEventRequest.getSessionID())) {
			log.error("Session id is Empty or null");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.sessionID.null",
					null, "api.error.usereventrequest.sessionID.null.code"));
		}

		Optional<Account> account = this.accountRepository.findById(userEventRequest.getAccountID());
		if (account.isEmpty()) {
			log.error("Account Not found with Id: {}", userEventRequest.getAccountID());
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found",
					new Object[] { userEventRequest.getAccountID() }, "api.error.account.not.found.code"));
		}
	}

	private void checkIsSessionEnd(String session) {
		log.debug("In side get Check Session is End");
		if (StringUtils.isBlank(session)) {
			log.error("Session id is Empty or null");
			return;
		}

		Optional<UserSession> userSession = this.userSessionRepository.findById(session);
		if (!userSession.isEmpty() && userSession.get().getEndTime() != null) {
			log.error("Session {} => is already Closed ", session);
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.usereventrequest.session.expired",
					new Object[] { session }, "api.error.usereventrequest.session.expired.code"));
		}
	}

	@Override
	public void checkRequestedAccountWithAPIKey(Long requestedAccountId) {
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();

		Account account = authentication.getAccount();

		if (requestedAccountId == null) {
			log.error("Account is Empty");
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.accountID.null", null,
					"api.error.accountID.null.code"));
		}

		if (!requestedAccountId.equals(account.getId())) {
			log.error("Invalid ApiKey for Account Id: {}", requestedAccountId);
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.apiKey.invalid", new Object[] {},
					"api.error.apiKey.invalid.code"));
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public UserEvent saveEndEvent(EnddedSession endSession) {
		UserEvent event = null;
		try {
			OffsetDateTime timeStap = DateUtils.parseDate(endSession.getStartTime()).plusSeconds(closeOffset);
			if (!StringUtils.isBlank(endSession.getLastEventTime())
					&& !StringUtils.isBlank(endSession.getLastPageLoadTime())) {
				OffsetDateTime lastEvent = DateUtils.parseDate(endSession.getLastEventTime());
				OffsetDateTime lastPage = DateUtils.parseDate(endSession.getLastPageLoadTime());
				int result = lastEvent.compareTo(lastPage);
				if (result >= 0) {
					timeStap = lastEvent;
				} else if (result < 0) {
					timeStap = lastPage;
				}
			} else if (!StringUtils.isBlank(endSession.getLastEventTime())) {
				timeStap = DateUtils.parseDate(endSession.getLastEventTime());
			} else if (!StringUtils.isBlank(endSession.getLastPageLoadTime())) {
				timeStap = DateUtils.parseDate(endSession.getLastPageLoadTime());
			}
			event = new UserEvent(endSession);
			event.setTimestamp(DateUtils.getCurrentTimestemp());
			userSessionRepository.deactivateUsersNotLoggedInSince(event.getSession(), timeStap);
			event = userEventRepository.save(event);
			return event;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return event;
	}
}
