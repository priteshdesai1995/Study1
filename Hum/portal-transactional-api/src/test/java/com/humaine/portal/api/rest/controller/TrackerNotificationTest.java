package com.humaine.portal.api.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.MailFromDomainNotVerifiedException;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.AccountTracker;
import com.humaine.portal.api.rest.repository.AccountTrackerRepository;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;
import com.humaine.portal.api.util.DateUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TrackerNotificationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AWSClientProviderBuilder cognitoBuilder;

	@Mock
	AmazonSimpleEmailService emailService;

	@MockBean
	private AccountTrackerRepository accountTrackerRepository;

	OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
	Account account = new Account(1L, "test.user@test.com");

	private String BASE_URI = "/trackernotification";

	private AccountTracker accTracker = new AccountTracker(1L, account, account.getEmail(), "1-tracker.js", false, "",
			timestemp, timestemp);
	List<AccountTracker> trackers = new ArrayList<AccountTracker>() {
		private static final long serialVersionUID = 3204327696568711828L;

		{
			add(new AccountTracker(1L, account, "jay.patel@bodegaswim.com", "1-tracker.js", false, "", timestemp,
					timestemp));
			add(new AccountTracker(2L, account, account.getEmail(), "1-tracker.js", false, "", timestemp, timestemp));
			add(new AccountTracker(3L, account, account.getEmail(), "1-tracker.js", false, "", timestemp, timestemp));
		}
	};

	@Before
	public void setup() {
		SendEmailResult result = new SendEmailResult();
		when(cognitoBuilder.getAmazonSimpleEmailServiceClient()).thenReturn(emailService);
		when(emailService.sendEmail(Mockito.any(SendEmailRequest.class))).thenReturn(result);
		when(accountTrackerRepository.save(Mockito.any(AccountTracker.class))).thenReturn(accTracker);
		when(accountTrackerRepository.getPendingAccountTracker()).thenReturn(trackers);
	}

	/**
	 * 
	 * Check Email Notification Sent
	 * 
	 */
	@Test
	void testEmailNotification() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BASE_URI).contentType(MediaType.APPLICATION_JSON)
				.header("version", "v1");
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}

}
