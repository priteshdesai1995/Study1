package com.humaine.collection.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.DateUtils;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.ErrorStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AccountSessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AccountRepository accountRepository;

	@Mock
	private SecurityContext securityContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private OffsetDateTime timestemp = OffsetDateTime.of(LocalDate.of(2015, 10, 18), LocalTime.of(11, 20, 30, 1000),
			DateUtils.getZoneOffset());

	private Account account = new Account(1L, "test@abc.com", "Test Account", "www.test.com", "ACTIVE", "GOLD", 200,
			timestemp, timestemp);

	@Before
	public void setUp() {
		org.springframework.security.core.userdetails.User usr = new org.springframework.security.core.userdetails.User(
				account.getEmail(), account.getEmail(), new HashSet<GrantedAuthority>());
		org.springframework.security.core.Authentication auth = new Authentication(usr, null, account);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

		Mockito.when(accountRepository.findAccountByAPIKey(Mockito.anyString())).thenReturn(account);
		Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
	}

	private String BASE_URI = "/sessionTimeout";
	private String KEY = "TESTKEY";

	/**
	 * Test Account Session Timeout
	 */
	@Test
	public void testGetAccountSessionTimeout() throws Exception {
		String requestURI = String.format("%s/%s", BASE_URI, account.getId());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(requestURI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").header(CustomAuthenticationFilter.HEADER, KEY);

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.timeout", is(account.getSessionTimeout()))).andReturn();

	}

	/**
	 * Test Account Session Timeout with Account Id null
	 */
	@Test
	public void testGetAccountSessionTimeout_With_Account_ID_Null() throws Exception {
		String requestURI = String.format("%s/%s", BASE_URI, "");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(requestURI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").header(CustomAuthenticationFilter.HEADER, KEY);

		mockMvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();

	}

	/**
	 * Test Account Session Timeout with Account Id Not Exist In Database
	 */
	@Test
	public void testGetAccountSessionTimeout_With_Unknow_Account_ID() throws Exception {
		Long accountId = 200L;
		String requestURI = String.format("%s/%s", BASE_URI, accountId);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(requestURI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").header(CustomAuthenticationFilter.HEADER, KEY);

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.apiKey.invalid", new Object[] {}))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.apiKey.invalid.code"))))
				.andReturn();

	}
}
