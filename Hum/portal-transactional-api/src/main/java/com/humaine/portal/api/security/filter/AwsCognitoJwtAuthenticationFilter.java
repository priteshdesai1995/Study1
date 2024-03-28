package com.humaine.portal.api.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.exception.ExceptionController;
import com.humaine.portal.api.filter.CORSFilter;
import com.humaine.portal.api.model.Account;
import com.humaine.portal.api.rest.repository.AccountRepository;
import com.humaine.portal.api.security.config.WebSecurityConfiguration;
import com.humaine.portal.api.security.exception.CognitoException;
import com.humaine.portal.api.util.ErrorMessageBuilder;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.TransactionInfo;
import com.nimbusds.jose.proc.BadJOSEException;

public class AwsCognitoJwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String ERROR_OCCURED_WHILE_PROCESSING_THE_TOKEN = "Invalid Access Token";
	private static final String INVALID_TOKEN_MESSAGE = "Invalid Access Token";

	private static final Logger log = LogManager.getLogger(AwsCognitoJwtAuthenticationFilter.class);

	private AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;
	
	@Autowired
	private AccountRepository accountRepository;
	
	public AwsCognitoJwtAuthenticationFilter(AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor) {
		this.awsCognitoIdTokenProcessor = awsCognitoIdTokenProcessor;
	}

	/**
	 * Creates an Exception Response
	 * 
	 */

	private void createExceptionResponse(ServletRequest request, ServletResponse response, CognitoException exception)
			throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		ExceptionController exceptionController = null;
		ObjectMapper objMapper = new ObjectMapper();

		// ExceptionController is now accessible because I loaded it manually
		exceptionController = appContext.getBean(ExceptionController.class);
		// Calls the exceptionController
		TransactionInfo responseWrapper = exceptionController.handleJwtException(req, exception);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
		wrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		CORSFilter.setHeader(req, wrapper);
		wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
		wrapper.getWriter().flush();

		
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = null;
		String pathInfo = request.getRequestURI().substring(request.getContextPath().length());

		try {
			authentication = awsCognitoIdTokenProcessor.getAuthentication((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Account account = accountRepository.findAccountByEmail(authentication.getName());
			if (account == null) {
				throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.details.not.found",
						new Object[] {}, "api.error.account.details.not.found.code"));
			}
			if (checkAccountRegistrationStatus(pathInfo)) {
				if (AccountStatus.unconfirmed.equals(account.getStatus())) {
					throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.account.confirmation.pending",
							new Object[] {}, "api.error.account.confirmation.pending.code"));
				}
				if (account.getBusinessInformation() == null) {
					throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.registration.pending",
							new Object[] { account.getId() }, "api.error.registration.pending.code"));
				}	
			}
			
		} catch (APIException e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage());
			createExceptionResponse(request, response, new CognitoException(ErrorMessageBuilder.retriveMessage(e.getMessage()),
					ErrorMessageBuilder.retriveCode(e.getMessage()), e.getMessage()));
			return;
		} catch (BadJOSEException e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage());
			createExceptionResponse(request, response, new CognitoException(INVALID_TOKEN_MESSAGE,
					CognitoException.INVALID_TOKEN_EXCEPTION_CODE, e.getMessage()));
			return;
		} catch (CognitoException e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage());
			createExceptionResponse(request, response, new CognitoException(e.getErrorMessage(),
					CognitoException.INVALID_TOKEN_EXCEPTION_CODE, e.getDetailErrorMessage()));
			return;
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			log.error(e.getMessage());
			createExceptionResponse(request, response, new CognitoException(ERROR_OCCURED_WHILE_PROCESSING_THE_TOKEN,
					CognitoException.INVALID_TOKEN_EXCEPTION_CODE, e.getMessage()));
			return;
		}

		filterChain.doFilter(request, response);
	}

	private boolean checkAccountRegistrationStatus(String path) {
		boolean result = true;
		String[] publicPaths = WebSecurityConfiguration.securePathWithOutCheckingAccountRegistrationStatus;
		for (int i = 0; i < publicPaths.length; i++) {
			if (publicPaths[i].startsWith(path)) {
				result = false;
				break;
			}

		}
		return result;
	}
}
