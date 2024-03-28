package com.humaine.portal.api.rest.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BusinessInformation;
import com.humaine.portal.api.model.Page;
import com.humaine.portal.api.request.dto.PageMasterData;
import com.humaine.portal.api.request.dto.RegistrationRequest;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.security.exception.CognitoException;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ErrorStatus;
import com.humaine.portal.api.util.JsonUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AccountRepository accountRepository;

	@MockBean
	AWSClientProviderBuilder cognitoBuilder;

	@Mock
	AWSCognitoIdentityProvider provider;

	@Mock
	private SecurityContext securityContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	private BusinessInformation businessInfo = new BusinessInformation(1L, "Test Company", "www.test.com", "1122334455",
			"new street road", "New York", "1000$", "Magento", true, "Magento", "10", "500", "IT", "USA",
			"Need user visit solution", null, "New York", "New York", "USA");
	private Account account = new Account(1L, "wwww.test.com", AccountStatus.confirmed, "test", "email@test.com",
			businessInfo, 1000, timestemp, timestemp, Arrays.asList(new String[] { "IT", "SEO" }),
			Arrays.asList(new String[] { "ABC ANALYTICS" }), new ArrayList<Page>());

	private RegistrationRequest getRegistrationRequest() {
		return new RegistrationRequest(account.getId(), businessInfo.getName(), businessInfo.getAddress(),
				businessInfo.getPhoneNumber(), businessInfo.getCity(), businessInfo.getState(),
				businessInfo.getCountry(), account.getIndustries(), businessInfo.getUrl(),
				businessInfo.getEShopHosting(), businessInfo.getHeadquarterLocation(), businessInfo.getConsumersFrom(),
				businessInfo.getNoOfEmployees(), businessInfo.getNoOfProducts(), businessInfo.getHighestProductPrice(),
				businessInfo.getCurrentAnalyticsSolution(), businessInfo.getTrackingData(),
				account.getDataTrackingProviders(), businessInfo.getTrackingDataTypes(),
				businessInfo.getExpectationComment(), account.getSiteUrl(), new ArrayList<PageMasterData>());
	}

	@Before
	public void setUp() throws CognitoException, ParseException, BadJOSEException, JOSEException, Exception {
		Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(account);
		Mockito.when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(account);
		Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
		Mockito.when(cognitoBuilder.getAWSCognitoIdentityClient()).thenReturn(provider);
		User user = new User(account.getEmail(), "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
	}

	private String BASE_URI = "/register";

	/**
	 * Save Business Info
	 */
	@Test
	public void testSaveAccountDetails() throws Exception {
		this.account.setBusinessInformation(null);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(getRegistrationRequest()));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * Save Business Info: without Email Confirmation
	 */
	@Test
	public void testSaveAccountDetailsWithoutEmailConfirmation() throws Exception {
		this.account.setStatus(AccountStatus.unconfirmed);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(getRegistrationRequest()));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.account.confirmation.pending",
								new Object[] {}))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.account.confirmation.pending.code"))))
				.andReturn();
		this.account.setStatus(AccountStatus.confirmed);
	}

	/**
	 * Save Business Info: With Unknown Account
	 */
	@Test
	public void testSaveAccountDetailsWithOutSignup() throws Exception {
		RegistrationRequest request = this.getRegistrationRequest();
		request.setAccountID(10L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.account.not.found",
								new Object[] { request.getAccountID() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.account.not.found.code"))))
				.andReturn();
	}

	/**
	 * Save Business Info: That already saved
	 */
	@Test
	public void testSaveAccountDetailsThatAlreadySave() throws Exception {
		RegistrationRequest request = this.getRegistrationRequest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.user.already.register",
								new Object[] { account.getEmail() }))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.user.already.register.code"))))
				.andReturn();
	}

	/**
	 * get Account details
	 */
	@Test
	public void getAccountDetails() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * get Account details With Unknow Account
	 */
	@Test
	public void getAccountDetails_With_Unknown_Account() throws Exception {
		User user = new User("unknownemail@test.com", "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.account.details.not.found", new Object[] {}))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.account.details.not.found.code"))))
				.andReturn();
	}

	/**
	 * Update Business Info
	 */
	@Test
	public void testUpdateAccountDetails() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(getRegistrationRequest()));

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(ErrorStatus.SUCCESS.value()))).andReturn();
	}

	/**
	 * Update Business Info: without Email Confirmation
	 */
	@Test
	public void testUpdateAccountDetailsWithoutEmailConfirmation() throws Exception {
		this.account.setStatus(AccountStatus.unconfirmed);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(getRegistrationRequest()));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.account.confirmation.pending",
								new Object[] {}))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.account.confirmation.pending.code"))))
				.andReturn();
		this.account.setStatus(AccountStatus.confirmed);
	}

	/**
	 * Update Business Info: With Unknown Account
	 */
	@Test
	public void testUpdateAccountDetails_With_unknown_Account() throws Exception {
		User user = new User("unknownemail@test.com", "", new HashSet<GrantedAuthority>());
		Authentication auth = new CognitoJwtAuthentication(user, null, user.getAuthorities());
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
		RegistrationRequest request = this.getRegistrationRequest();
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1").content(JsonUtils.json(request));

		mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError())
				.andExpect(jsonPath("$.status", is(ErrorStatus.FAIL.value())))
				.andExpect(jsonPath("$.errorList[0].message",
						is(this.errorMessageUtils.getMessage("api.error.account.details.not.found", new Object[] {}))))
				.andExpect(jsonPath("$.errorList[0].code",
						is(this.errorMessageUtils.getMessage("api.error.account.details.not.found.code"))))
				.andReturn();
	}
}
