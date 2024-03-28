package com.humaine.portal.api.security.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.request.dto.ConfirmResetPassword;
import com.humaine.portal.api.request.dto.ResendSignupConfirmation;
import com.humaine.portal.api.request.dto.ResetPassword;
import com.humaine.portal.api.request.dto.SignInRequest;
import com.humaine.portal.api.request.dto.SignUp;
import com.humaine.portal.api.request.dto.SignupEmailConfirmation;
import com.humaine.portal.api.response.dto.CognitoUser;
import com.humaine.portal.api.response.dto.SignUpResponse;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.security.exception.CognitoException;
import com.humaine.portal.api.security.model.SpringSecurityUser;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.ErrorMessageUtils;

@Service
public class CognitoAuthenticationService {
	private static final Logger log = LogManager.getLogger(CognitoAuthenticationService.class);

	private static final String PASS_WORD = "PASSWORD";

	private static final String USERNAME = "USERNAME";

	@Autowired
	AWSClientProviderBuilder cognitoBuilder;

	@Autowired
	private AWSConfig cognitoConfig;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Value("${account.session.timeout}")
	private Integer timeout;

	private AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {

		return cognitoBuilder.getAWSCognitoIdentityClient();

	}

	public SpringSecurityUser authenticate(SignInRequest request) throws Exception {
		AuthenticationResultType authenticationResult = null;
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		try {
			final Map<String, String> authParams = new HashMap<>();
			authParams.put(USERNAME, request.getUsername());
			authParams.put(PASS_WORD, request.getPassword());

			final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
			authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH).withClientId(cognitoConfig.getClientId())
					.withUserPoolId(cognitoConfig.getPoolId()).withAuthParameters(authParams);

			AdminInitiateAuthResult result = cognitoClient.adminInitiateAuth(authRequest);

			authenticationResult = result.getAuthenticationResult();

			SpringSecurityUser userAuthenticated = new SpringSecurityUser(request.getUsername(), request.getPassword(),
					null, null, null);

			userAuthenticated.setAccessToken(authenticationResult.getAccessToken());
			userAuthenticated.setExpiresIn(authenticationResult.getExpiresIn());
			userAuthenticated.setTokenType(authenticationResult.getTokenType());
			userAuthenticated.setRefreshToken(authenticationResult.getRefreshToken());
			userAuthenticated.setIdToken(authenticationResult.getIdToken());
			Account account = this.accountRepository.findAccountByEmail(request.getUsername());
			if (account == null) {
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.found.with.username",
						new Object[] { request.getUsername() }, "api.error.account.found.with.username"));
			}

			if (AccountStatus.unconfirmed.equals(account.getStatus())) {
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
						new Object[] { request.getUsername() }, "api.error.account.confirmation.pending.code"));
			}
			log.info("User successfully authenticated userInfo: username {}", request.getUsername());

			return userAuthenticated;
		} catch (UserNotConfirmedException e) {
			throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
					new Object[] { request.getUsername() }, "api.error.account.confirmation.pending.code"));
		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (APIException e) {
			throw new APIException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}

	}

	public SignUpResponse signUp(SignUp signUpRequest) throws Exception {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
		log.info("creating user {}", signUpRequest.getEmail());
		Account acc = accountRepository.findAccountByUsername(signUpRequest.getUsername());
		if (acc != null) {
			throw new APIException(errorMessageUtils.getMessageWithCode(
					"api.error.user.registration.username.already.exist", new Object[] { signUpRequest.getUsername() },
					"api.error.user.registration.username.already.exist.code"));
		}
		acc = accountRepository.findAccountByEmail(signUpRequest.getEmail());
		if (acc != null) {
			throw new APIException(errorMessageUtils.getMessageWithCode(
					"api.error.user.registration.email.already.exist", new Object[] { signUpRequest.getEmail() },
					"api.error.user.registration.email.already.exist.code"));
		}
		try {
			SignUpRequest req = new SignUpRequest().withUsername(signUpRequest.getEmail())
					.withPassword(signUpRequest.getPassword()).withClientId(cognitoConfig.getClientId())
					.withUserAttributes(new AttributeType().withName("email").withValue(signUpRequest.getEmail()),
							new AttributeType().withName(cognitoConfig.getCustomUsername())
									.withValue(signUpRequest.getUsername()));

			SignUpResult result = cognitoClient.signUp(req);

//			Account account = new Account();
//			account.setUsername(signUpRequest.getUsername());
//			account.setEmail(signUpRequest.getEmail());
//			account.setStatus(AccountStatus.unconfirmed);
//			OffsetDateTime timestemp = DateUtils.getCurrentTimestemp();
//			account.setCreatedOn(timestemp);
//			account.setModifiedOn(timestemp);
//			account.setSessionTimeout(timeout);
//			account = accountRepository.save(account);
//			return new SignUpResponse(account.getUsername(), account.getId(), account.getStatus());
			return null;

		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}
	}

	public Long confirmSignupEmail(SignupEmailConfirmation emailConfirm) throws Exception {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		log.info("Confirm Email Request {}", emailConfirm.getEmail());

		try {
//			Account account = accountRepository.findAccountByEmail(emailConfirm.getEmail());
//			if (account == null) {
//				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found.email",
//						new Object[] { emailConfirm.getEmail() }, "api.error.account.not.found.email.code"));
//			}

//			if (AccountStatus.confirmed.equals(account.getStatus())) {
//				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found.email",
//						new Object[] { emailConfirm.getEmail() }, "api.error.account.not.found.email.code"));
//			}
//			ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest().withUsername(account.getUsername())
//					.withConfirmationCode(emailConfirm.getCode()).withClientId(cognitoConfig.getClientId());
			ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest().withUsername(emailConfirm.getEmail())
					.withConfirmationCode(emailConfirm.getCode()).withClientId(cognitoConfig.getClientId());

			ConfirmSignUpResult result = cognitoClient.confirmSignUp(confirmRequest);
//			account.setStatus(AccountStatus.confirmed);
//			this.accountRepository.save(account);
//			log.info("User Account Confirmed {} ", account);
//			return account.getId();
			return 1L;
		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (APIException e) {
			throw new APIException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}
	}

	public void resendSignUpConfirmationCode(ResendSignupConfirmation request) throws Exception {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		log.info("Resend Email Confirmation CodeRequest {}", request.getEmail());

		try {
			Account account = accountRepository.findAccountByEmail(request.getEmail());
			if (account == null) {
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found.email",
						new Object[] { request.getEmail() }, "api.error.account.not.found.email.code"));
			}
			if (AccountStatus.confirmed.equals(account.getStatus())) {
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.account.not.found.email",
						new Object[] { request.getEmail() }, "api.error.account.not.found.email.code"));
			}
//			ResendConfirmationCodeRequest resendConfirmationCode = new ResendConfirmationCodeRequest()
//					.withUsername(account.getUsername()).withClientId(cognitoConfig.getClientId());
			ResendConfirmationCodeRequest resendConfirmationCode = new ResendConfirmationCodeRequest()
					.withUsername(account.getEmail()).withClientId(cognitoConfig.getClientId());

			ResendConfirmationCodeResult result = cognitoClient.resendConfirmationCode(resendConfirmationCode);
			log.info("User Account Confirmed {} ", account);

		} catch (APIException e) {
			throw e;
		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}
	}

	public CognitoUser getUserInfo(String accessToken) throws CognitoException {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
		try {

			if (StringUtils.isBlank(accessToken)) {
				throw new CognitoException("User must provide an access token",
						CognitoException.INVALID_ACCESS_TOKEN_EXCEPTION, "User must provide an access token");
			}

			GetUserRequest userRequest = new GetUserRequest().withAccessToken(accessToken);

			GetUserResult userResult = cognitoClient.getUser(userRequest);
			List<AttributeType> userAttributes = userResult.getUserAttributes();
			CognitoUser userResponse = getUserAttributesData(userAttributes, userResult.getUsername());

			return userResponse;

		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}

	}

	private CognitoUser getUserAttributesData(List<AttributeType> userAttributes, String username) {
		CognitoUser userResponse = new CognitoUser();

		userResponse.setUsername(username);

		for (AttributeType attribute : userAttributes) {
			if (attribute.getName().equals("email")) {
				userResponse.setEmail(attribute.getValue());
			}
		}

		return userResponse;
	}

	public void resetPassword(ResetPassword request) throws Exception {
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();
		String username = request.getUsername();
		log.info("Reset password {}", username);

		try {

			// If username is blank it throws an error
			if (StringUtils.isBlank(username)) {
				throw new CognitoException("Invalid username", CognitoException.INVALID_USERNAME_EXCEPTION,
						"Invalid username");
			}

			ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
					.withClientId(cognitoConfig.getClientId()).withUsername(username);

			ForgotPasswordResult forgotPasswordResult = cognitoClient.forgotPassword(forgotPasswordRequest);

			log.info("Reset password response Delivery Details: {} ", forgotPasswordResult.getCodeDeliveryDetails());

		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (CognitoException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}

	}

	public void confirmResetPassword(ConfirmResetPassword passwordRequest) throws Exception {
		String username = passwordRequest.getUsername();
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		if (log.isInfoEnabled()) {
			log.info("Confirm Reset Password {}", username);
		}

		try {

			ConfirmForgotPasswordRequest forgotPasswordRequest = new ConfirmForgotPasswordRequest()
					.withClientId(cognitoConfig.getClientId()).withUsername(username)
					.withPassword(passwordRequest.getNewPassword()).withConfirmationCode(passwordRequest.getCode());

			cognitoClient.confirmForgotPassword(forgotPasswordRequest);

			if (log.isInfoEnabled()) {
				log.info("Confirm Reset Password successful for {} ", username);
			}

		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getErrorMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}

	}

	public String signOut(String accessToken) throws Exception {
		String resultMessage = null;
		AWSCognitoIdentityProvider cognitoClient = getAmazonCognitoIdentityClient();

		String username = "";

		log.info("User sign out request {}", username);

		try {

			if (null != accessToken) {
				GlobalSignOutRequest globalSignOutRequest = new GlobalSignOutRequest().withAccessToken(accessToken);

				cognitoClient.globalSignOut(globalSignOutRequest);
				resultMessage = "SUCCESS";

				log.info("User signed out {}", username);

				return resultMessage;

			} else {
				throw new CognitoException("Missing access token", CognitoException.ACCESS_TOKEN_MISSING_EXCEPTION,
						"Missing access token");
			}

		} catch (com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), e.getErrorCode(), e.getMessage() + e.getErrorCode());
		} catch (CognitoException cognitoException) {
			throw cognitoException;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CognitoException(e.getMessage(), CognitoException.GENERIC_EXCEPTION_CODE, e.getMessage());
		}
	}
}
