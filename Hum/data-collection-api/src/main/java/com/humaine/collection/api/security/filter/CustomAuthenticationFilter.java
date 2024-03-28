package com.humaine.collection.api.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.collection.api.enums.AccountStatus;
import com.humaine.collection.api.exception.APIException;
import com.humaine.collection.api.exception.ExceptionController;
import com.humaine.collection.api.model.Account;
import com.humaine.collection.api.rest.repository.AccountRepository;
import com.humaine.collection.api.security.config.Authentication;
import com.humaine.collection.api.util.ErrorMessageUtils;
import com.humaine.collection.api.util.TransactionInfo;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

	private static final String EMPTY_STRING = "";

	public static final String HEADER = "API-KEY";

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Autowired
	private AccountRepository accountRepository;

	private void createExceptionResponse(ServletRequest request, ServletResponse response, APIException exception)
			throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		ExceptionController exceptionController = null;
		ObjectMapper objMapper = new ObjectMapper();

		// ExceptionController is now accessible because I loaded it manually
		exceptionController = appContext.getBean(ExceptionController.class);
		// Calls the exceptionController
		ResponseEntity<TransactionInfo> responseWrapper = exceptionController.handleAPIexception(exception);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) response);
		wrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
		wrapper.getWriter().flush();

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = null;

		HttpServletRequest htttpRequest = (HttpServletRequest) request;

		String apiKey = request.getHeader(HEADER);

		try {
			if (apiKey == null) {
				throw new APIException(errorMessageUtils.getMessageWithCode("api.error.apiKey.not.found", null,
						"api.error.apiKey.not.found.code"));
			} else {
				Account acc = accountRepository.findAccountByAPIKey(apiKey);
				if (acc == null || acc != null && acc.getBusinessInformation() == null
						|| acc != null && acc.getBusinessInformation() != null
								&& !AccountStatus.confirmed.value().equals(acc.getStatus())) {
					throw new APIException(errorMessageUtils.getMessageWithCode("api.error.apiKey.invalid", null,
							"api.error.apiKey.invalid.code"));
				}
				List<String> groups = new ArrayList<>();
				List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
				User user = new User(acc.getEmail(), EMPTY_STRING, grantedAuthorities);
				Authentication auth = new Authentication(user, grantedAuthorities, acc);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			createExceptionResponse(request, response, new APIException(e.getMessage()));
			return;
		}

		filterChain.doFilter(request, response);
	}
}
