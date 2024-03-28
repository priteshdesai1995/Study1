package com.humaine.collection.api.boundary;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.humaine.collection.api.config.SpringContext;
import com.humaine.collection.api.enums.AccountStatus;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.EventData;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.rest.repository.MouseEventRecordingRepository;
import com.humaine.collection.api.rest.service.UserSessionService;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;

@ServerEndpoint(value = "/eventRecording")
public class SocketEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketEndpoint.class);

	private static final String EMPTY_STRING = "";

	private static Long accountID;
	private static String userID;
	private static String sessionID;

	private final MouseEventRecordingRepository eventRecordingRepository;

	private final ErrorMessageUtils errorMessageUtils;

	private final AccountRepository accountRepository;

	private final UserSessionService userSessionService;

	public SocketEndpoint() {
		this.eventRecordingRepository = (MouseEventRecordingRepository) SpringContext.getApplicationContext()
				.getBean("mouseEventRecordingRepository");
		this.accountRepository = (AccountRepository) SpringContext.getApplicationContext().getBean("accountRepository");
		this.errorMessageUtils = (ErrorMessageUtils) SpringContext.getApplicationContext().getBean("errorMessageUtils");
		this.userSessionService = (UserSessionService) SpringContext.getApplicationContext()
				.getBean(UserSessionService.class);
	}

	@OnOpen
	public void onOpen(Session session) {
		LOGGER.info("onOpen call");
		try {
			List<String> apiKeyFromParamMap = session.getRequestParameterMap().get("APIKEY");
			List<String> accountIdFromParamMap = session.getRequestParameterMap().get("accountID");
			sessionID = Joiner.on("").join(session.getRequestParameterMap().get("sessionID"));
			userID = Joiner.on("").join(session.getRequestParameterMap().get("userID"));
			if (apiKeyFromParamMap == null || accountIdFromParamMap == null) {
				session.close();
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.apiKey.or.account.not.found",
						null, "api.error.apiKey.or.account.not.found.code"));
			} else {
				String apiKey = Joiner.on("").join(apiKeyFromParamMap);
				accountID = Long.valueOf(Joiner.on("").join(session.getRequestParameterMap().get("accountID")));
				this.verifyApiKey(apiKey);
				this.userSessionService.checkRequestedAccountWithAPIKey(accountID);
				UserSession validation = this.userSessionService.validateUserSessionValidation(sessionID, null);
				if (validation != null) {
					throw new APIException(errorMessageUtils.getMessageWithCode("api.error.session.notfound", null,
							"api.error.session.notfound.code"));
				}
			}
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void verifyApiKey(String apiKey) {
		Account acc = accountRepository.findAccountByAPIKey(apiKey);
		if (acc == null || acc != null && acc.getBusinessInformation() == null || acc != null
				&& acc.getBusinessInformation() != null && !AccountStatus.confirmed.value().equals(acc.getStatus())) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.apiKey.invalid", null,
					"api.error.apiKey.invalid.code"));
		}
		List<String> groups = new ArrayList<>();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		User user = new User(acc.getEmail(), EMPTY_STRING, grantedAuthorities);
		Authentication auth = new Authentication(user, grantedAuthorities, acc);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		Gson gson = new Gson();
		EventData eventData = new EventData();
		eventData = gson.fromJson(message, EventData.class);
		OffsetDateTime currentDate = DateUtils.getCurrentTimestemp();
		eventData.setCreatedOn(currentDate);
		eventData.setModifiedOn(currentDate);
		eventData.setAccount(Long.valueOf(accountID));
		eventData.setUserId(userID);
		eventData.setSessionId(sessionID);
		if (eventData != null) {
			eventRecordingRepository.save(eventData);
		}
	}

	@OnClose
	public void onClose(Session session) {
		LOGGER.info("onClose " + session.getId());
	}

	@OnError
	public void onError(Throwable t) {
		LOGGER.error(t.getMessage(), t);
	}
}