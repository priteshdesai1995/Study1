package com.humaine.portal.api.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.request.dto.ConfirmResetPassword;
import com.humaine.portal.api.request.dto.ResendSignupConfirmation;
import com.humaine.portal.api.request.dto.ResetPassword;
import com.humaine.portal.api.request.dto.SignInRequest;
import com.humaine.portal.api.request.dto.SignUp;
import com.humaine.portal.api.request.dto.SignupEmailConfirmation;
import com.humaine.portal.api.response.dto.AuthenticationResponse;
import com.humaine.portal.api.response.dto.CognitoUser;
import com.humaine.portal.api.response.dto.EmailConfirmaionResponse;
import com.humaine.portal.api.response.dto.SignUpResponse;
import com.humaine.portal.api.rest.service.AccountService;
import com.humaine.portal.api.security.filter.AwsCognitoIdTokenProcessor;
import com.humaine.portal.api.security.service.CognitoAuthenticationService;
import com.humaine.portal.api.util.Constants;
import com.humaine.portal.api.util.EncryptDecrypt;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.SecretKeyUtils;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Account Onboarding", description = "Account Signup and Registration", authorizations = {})
public class AuthenticationController {

	@Autowired(required = false)
	private AuthenticationManager authenticationManager;

	@Autowired(required = false)
	private CognitoAuthenticationService authService;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private AccountService accountService;

	@Autowired
	private EncryptDecrypt encryptDecrypt;

	@PostMapping("login")
	@ApiOperation(value = "Login", notes = "User Login")
	public ResponseEntity<TransactionInfo> authenticationRequest(@Valid @RequestBody SignInRequest signInRequest)
			throws Exception {
//		Account account = this.accountService.getAccountByEmail(signInRequest.getEmail());
		signInRequest.setPassword(encryptDecrypt.decrypt(signInRequest.getPassword()));
		signInRequest.setUsername("admin");
		Long expiresIn;
		String token = null;
		String accessToken = null;
		String username = signInRequest.getUsername();
		String password = signInRequest.getPassword();

		Map<String, String> credentials = new HashMap<>();
		credentials.put(Constants.PASS_WORD_KEY, password);
		// throws authenticationException if it fails !
		Authentication authentication = this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, credentials));

		Map<String, String> authenticatedCredentials = (Map<String, String>) authentication.getCredentials();
		token = authenticatedCredentials.get(Constants.ID_TOKEN_KEY);
		expiresIn = Long.parseLong(authenticatedCredentials.get(Constants.EXPIRES_IN_KEY));
		accessToken = authenticatedCredentials.get(Constants.ACCESS_TOKEN_KEY);
		CognitoUser userResponse = authService.getUserInfo(accessToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		// Return the token
		return ResponseBuilder.buildResponse(new AuthenticationResponse(token, expiresIn, accessToken));
	}

	@PostMapping(value = "signup", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Signup", notes = "User Signup")
	public ResponseEntity<TransactionInfo> signUpRequest(@Valid @RequestBody SignUp signUpRequest) throws Exception {
		SignUpResponse user = null;
		signUpRequest.setPassword(encryptDecrypt.decrypt(signUpRequest.getPassword()));
		user = authService.signUp(signUpRequest);
		return ResponseBuilder.buildResponse(user);
	}

	@PostMapping("signup/email")
	@ApiOperation(value = "Signup Email Verification", notes = "Verify Email After Signup")
	public ResponseEntity<TransactionInfo> signVerifyEmail(@Valid @RequestBody SignupEmailConfirmation request)
			throws Exception {
		Long accountId = authService.confirmSignupEmail(request);
		EmailConfirmaionResponse result = new EmailConfirmaionResponse(accountId);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode(
				"api.success.signup.email.verify", null, "api.success.signup.email.verify.code"), result);
	}

//	@PostMapping("resend/code")
//	@ApiOperation(value = "Resend Signup Verification Code", notes = "Resend Signup Email Verification Code")
	public ResponseEntity<TransactionInfo> resendConfirmCodeSignup(@Valid @RequestBody ResendSignupConfirmation request)
			throws Exception {

		authService.resendSignUpConfirmationCode(request);
		return ResponseBuilder.buildMessageCodeResponse(
				errorMessageUtils.getMessageWithCode("api.success.signup.resend.email.verification", null,
						"api.success.signup.resend.email.verification.code"));

	}

//	@PostMapping("reset/password")
//	@ApiOperation(value = "Reset Password", notes = "Api for Request for Reset Password. will send otp to register email")
	public ResponseEntity<TransactionInfo> resetPassword(@Valid @RequestBody ResetPassword request) throws Exception {
		Account account = this.accountService.getAccountByEmail(request.getEmail());
		request.setUsername(account.getEmail());
		authService.resetPassword(request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.reset.password", null, "api.success.reset.password.code"));
	}

//	@PostMapping("confirm/reset/password")
//	@ApiOperation(value = "Reset Password Confirmation", notes = "Verify Otp and reset Password")
	public ResponseEntity<TransactionInfo> confirmResetPassword(@Valid @RequestBody ConfirmResetPassword request)
			throws Exception {
		Account account = this.accountService.getAccountByEmail(request.getEmail());
		request.setNewPassword(encryptDecrypt.decrypt(request.getNewPassword()));
		request.setUsername(account.getEmail());
		authService.confirmResetPassword(request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode(
				"api.success.reset.password.verify", null, "api.success.reset.password.verify.code"));
	}

//	@PostMapping("signout")
//	@ApiOperation(value = "Signout", notes = "Signout Api Call")
	public ResponseEntity<TransactionInfo> signOut(@RequestHeader("sessionToken") String session) throws Exception {
		final String origanlToken = AwsCognitoIdTokenProcessor.extractAndDecodeJwt(session);
		this.authService.signOut(origanlToken);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils.getMessageWithCode("api.success.user.signout",
				null, "api.success.user.signout.code"));
	}
}
