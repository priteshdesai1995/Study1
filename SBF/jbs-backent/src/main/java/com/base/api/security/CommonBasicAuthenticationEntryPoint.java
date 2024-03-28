package com.base.api.security;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.base.api.utils.ErrorStatus;
import com.base.api.utils.TransactionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommonBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();

		//response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        

		TransactionInfo<String> transactionInfo = new TransactionInfo<String>(ErrorStatus.FAIL.toString());
		transactionInfo.addErrorList(authEx.getMessage());
		transactionInfo.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		writer.println(objectMapper.writeValueAsString(
				new ResponseEntity<TransactionInfo<String>>(transactionInfo, HttpStatus.UNAUTHORIZED)));
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName("CRM_REALM");
		super.afterPropertiesSet();
	}
}
