package com.humaine.collection.api.rest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.model.UserSession;
import com.humaine.collection.api.request.dto.UserEventRequest;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.rest.repository.UserSessionRepository;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSessionServiceTest {

	@Autowired
	private UserSessionService userSessionService;

	@MockBean
	private UserSessionRepository userSessionRepository;

	@MockBean
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private OffsetDateTime timestemp = OffsetDateTime.of(LocalDate.of(2015, 10, 18), LocalTime.of(11, 20, 30, 1000),
			DateUtils.getZoneOffset());

	private UserSession userSession = new UserSession("SES101", "USER101", 1L, "DEVICEID", "New York", "New York",
			"USA", 51.5074D, 0.1278, timestemp, null);
	private Account account = new Account(userSession.getAccount(), "Test Account", "www.test.com", "ACTIVE", "GOLD", 0,
			timestemp, timestemp);

	@Before
	public void setUp() {
		Mockito.when(userSessionRepository.findById(userSession.getId())).thenReturn(Optional.of(userSession));
		Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
	}

	/**
	 * Test Checkout session Exist
	 */
	@Test
	public void testCheckSessionExist() {
		assertThat(userSessionService.checkSessionExist(userSession.getId())).isEqualTo(true);
	}

	/**
	 * Test Checkout session With Vlaue that not exist in Database
	 */
	@Test
	public void testCheckSessionExist_With_Unknown_SessionId() {
		assertThat(userSessionService.checkSessionExist("SES400")).isEqualTo(false);
	}

	/**
	 * Test Checkout session With Session Id null
	 */
	@Test
	public void testCheckSessionExist_With_SessionId_Null() {
		assertThat(userSessionService.checkSessionExist("SES400")).isEqualTo(false);
	}

	/**
	 * Test Validate session with Session Id That Exist
	 */
	@Test
	public void testValidateSession() {
		assertDoesNotThrow(() -> {
			userSessionService.validateSession(userSession.getId());
		});
	}

	/**
	 * Test Validate session with Session Id That Dies not Exist in DB
	 */
	@Test
	public void testValidateSession_With_Unknow_SessionId() {
		assertDoesNotThrow(() -> {
			userSessionService.validateSession(userSession.getId());
		});
	}

	/**
	 * Test Start session
	 */
	@Test
	public void testStartUserSession() {
		UserEventRequest request = new UserEventRequest();
		request.setSessionID("SES400");
		request.setAccountID(userSession.getAccount());
		UserSession resul = userSessionService.startUsersession(request, timestemp);
		assertThat(resul.getId()).isEqualTo(request.getSessionID());
		assertThat(resul.getAccount()).isEqualTo(request.getAccountID());
	}

	/**
	 * Test Start session that already Started
	 */
	@Test
	public void testStartUserSession_With_Already_Started_Session() {
		UserEventRequest request = new UserEventRequest();
		request.setSessionID(userSession.getId());
		request.setAccountID(userSession.getAccount());
		Exception exception = assertThrows(APIException.class, () -> {
			userSessionService.startUsersession(request, timestemp);
		});

		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.session.already.start",
				new Object[] { request.getSessionID() }, "api.error.session.already.start.code");
		assertEquals(expectedOutput, exception.getMessage());
	}

	/**
	 * Test End session
	 */
	@Test
	public void testEndtUserSession() {
		UserEventRequest request = new UserEventRequest();
		request.setSessionID(userSession.getId());
		request.setAccountID(userSession.getAccount());
		UserSession resul = userSessionService.endUsersession(request, timestemp);
		assertThat(request.getSessionID()).isEqualTo(resul.getId());
		assertThat(request.getAccountID()).isEqualTo(resul.getAccount());
	}

	/**
	 * Test End session that not Started Yet
	 */
	@Test
	public void testEndUserSession_With_Already_Started_Session() {
		UserEventRequest request = new UserEventRequest();
		request.setSessionID("UNKNOW_SESSION_ID");
		request.setAccountID(userSession.getAccount());
		Exception exception = assertThrows(APIException.class, () -> {
			userSessionService.endUsersession(request, timestemp);
		});

		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.session.notfound",
				new Object[] { request.getSessionID() }, "api.error.session.notfound.code");
		assertEquals(expectedOutput, exception.getMessage());
	}
}
