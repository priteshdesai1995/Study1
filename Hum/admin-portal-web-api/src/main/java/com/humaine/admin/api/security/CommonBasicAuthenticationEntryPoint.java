package com.humaine.admin.api.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.admin.api.util.ErrorField;
import com.humaine.admin.api.util.ErrorStatus;
import com.humaine.admin.api.util.TransactionInfo;

@Component("commonBasicAuthenticationEntryPoint")
public class CommonBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException, ServletException {

		ObjectMapper objMapper = new ObjectMapper();

		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(new ErrorField(String.valueOf(HttpStatus.SC_FORBIDDEN), "Invalid access token"));
		transactionInfo.setStatusCode(HttpStatus.SC_UNAUTHORIZED);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setStatus(HttpStatus.SC_UNAUTHORIZED);
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(transactionInfo));
		wrapper.getWriter().flush();

	}
}

//	@Override
//	public void afterPropertiesSet() {
//		setRealmName("CRM_REALM");
//		super.afterPropertiesSet();
//	}
