package com.humaine.portal.api.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.model.BusinessInformation;
import com.humaine.portal.api.model.Page;
import com.humaine.portal.api.request.dto.PageMasterData;
import com.humaine.portal.api.request.dto.RegistrationRequest;
import com.humaine.portal.api.request.dto.SignUp;
import com.humaine.portal.api.request.dto.SignupEmailConfirmation;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.security.exception.CognitoException;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CognitoAuthenticationServiceTest {

	@Autowired
	private CognitoAuthenticationService cognitoService;

	@MockBean
	AWSClientProviderBuilder cognitoBuilder;

	@Mock
	AWSCognitoIdentityProvider provider;

	@Autowired
	private AWSConfig cognitoConfig;

	@MockBean
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	private OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();

	@Before
	public void init() {
		Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(account);
		Mockito.when(accountRepository.findAccountByUsername(account.getUsername())).thenReturn(account);
		Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);
		Mockito.when(cognitoBuilder.getAWSCognitoIdentityClient()).thenReturn(provider);
	}

	private BusinessInformation businessInfo = new BusinessInformation(1L, "Test Company", "www.test.com", "1122334455",
			"new street road", "New York", "1000$", "Magento", true, "Magento", "10", "500", "IT", "USA",
			"Need user visit solution", null, "New York", "New York", "USA");
	private Account account = new Account(1L, "wwww.test.com", AccountStatus.confirmed, "testusername",
			"email@test.com", businessInfo, 1000, timestemp, timestemp, Arrays.asList(new String[] { "IT", "SEO" }),
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

	private final String password = "Test@1234";

	private final String signupOtp = "111111";

	/*
	 * User Signup with Username that already exist
	 */
	@Test
	public void signUp_with_exist_username() {
		SignUpRequest req = new SignUpRequest().withUsername(account.getEmail()).withPassword(password)
				.withClientId(cognitoConfig.getClientId())
				.withUserAttributes(new AttributeType().withName("email").withValue(account.getEmail()));
		SignUpResult result = new SignUpResult();
		result.setUserConfirmed(false);
		Mockito.when(provider.signUp(req)).thenReturn(result);
		SignUp signup = new SignUp(account.getUsername(), account.getEmail(), password);
		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.user.registration.username.already.exist",
				new Object[] { signup.getUsername() }, "api.error.user.registration.username.already.exist.code");
		Exception exception = assertThrows(APIException.class, () -> {
			cognitoService.signUp(signup);
		});

		assertEquals(expectedOutput, exception.getMessage());
	}

	/*
	 * User Signup with Email that already exist
	 */
	@Test
	public void signUp_with_exist_email() {
		SignUpRequest req = new SignUpRequest().withUsername(account.getEmail()).withPassword(password)
				.withClientId(cognitoConfig.getClientId())
				.withUserAttributes(new AttributeType().withName("email").withValue(account.getEmail()));
		SignUpResult result = new SignUpResult();
		result.setUserConfirmed(false);
		Mockito.when(provider.signUp(req)).thenReturn(result);
		SignUp signup = new SignUp("newUsername", account.getEmail(), password);
		var expectedOutput = errorMessageUtils.getMessageWithCode("api.error.user.registration.email.already.exist",
				new Object[] { signup.getEmail() }, "api.error.user.registration.email.already.exist.code");
		Exception exception = assertThrows(APIException.class, () -> {
			cognitoService.signUp(signup);
		});

		assertEquals(expectedOutput, exception.getMessage());
	}

	/*
	 * User Signup
	 */
	@Test
	public void signUp() throws Exception {
		SignUpRequest req = new SignUpRequest().withUsername(account.getEmail()).withPassword(password)
				.withClientId(cognitoConfig.getClientId())
				.withUserAttributes(new AttributeType().withName("email").withValue(account.getEmail()));
		SignUpResult result = new SignUpResult();
		result.setUserConfirmed(false);
		Mockito.when(provider.signUp(req)).thenReturn(result);
		SignUp signup = new SignUp("newUsername", "newemail@test.com", password);
		cognitoService.signUp(signup);
	}

	/*
	 * Confirm User Signup with Wrong OTP
	 */
	@Test
	public void signUp_confirm_email_with_wrong_otp() {
		ConfirmSignUpRequest req = new ConfirmSignUpRequest().withUsername(account.getEmail())
				.withConfirmationCode(signupOtp).withClientId(cognitoConfig.getClientId());
		ConfirmSignUpResult result = new ConfirmSignUpResult();
		account.setStatus(AccountStatus.unconfirmed);
		String invalidOTP = "Invalid OTP";
		Mockito.when(provider.confirmSignUp(Mockito.any(ConfirmSignUpRequest.class)))
				.thenAnswer(new Answer<ConfirmSignUpResult>() {
					public ConfirmSignUpResult answer(InvocationOnMock invocation)
							throws AWSCognitoIdentityProviderException {
						ConfirmSignUpRequest val = invocation.getArgument(0, ConfirmSignUpRequest.class);
						System.out.println(val);
						if (!val.getConfirmationCode().equals(signupOtp)) {
							throw new AWSCognitoIdentityProviderException(invalidOTP);
						}
						return result;
					}
				});

		SignupEmailConfirmation confirmReq = new SignupEmailConfirmation(account.getEmail(), "222222");

		Exception exception = assertThrows(CognitoException.class, () -> {
			cognitoService.confirmSignupEmail(confirmReq);
		});
		System.out.println(exception.getMessage());
		assertEquals(invalidOTP, exception.getMessage());
		account.setStatus(AccountStatus.confirmed);
	}

	/*
	 * Confirm User Signup with Correct OTP
	 */
	@Test
	public void signUp_confirm_email() throws Exception {
		ConfirmSignUpRequest req = new ConfirmSignUpRequest().withUsername(account.getEmail())
				.withConfirmationCode(signupOtp).withClientId(cognitoConfig.getClientId());
		ConfirmSignUpResult result = new ConfirmSignUpResult();
		account.setStatus(AccountStatus.unconfirmed);
		String invalidOTP = "Invalid OTP";
		Mockito.when(provider.confirmSignUp(Mockito.any(ConfirmSignUpRequest.class)))
				.thenAnswer(new Answer<ConfirmSignUpResult>() {
					public ConfirmSignUpResult answer(InvocationOnMock invocation)
							throws AWSCognitoIdentityProviderException {
						ConfirmSignUpRequest val = invocation.getArgument(0, ConfirmSignUpRequest.class);
						if (!val.getConfirmationCode().equals(signupOtp)) {
							throw new AWSCognitoIdentityProviderException(invalidOTP);
						}
						return result;
					}
				});

		SignupEmailConfirmation confirmReq = new SignupEmailConfirmation(account.getEmail(), signupOtp);

		cognitoService.confirmSignupEmail(confirmReq);
		account.setStatus(AccountStatus.confirmed);
	}
}
