package com.base.api.security;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.base.api.utils.ErrorStatus;
import com.base.api.utils.TransactionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		ObjectMapper objMapper = new ObjectMapper();
		TransactionInfo<Object> transactionInfo = new TransactionInfo<Object>(ErrorStatus.FAIL);

		transactionInfo.addErrorList("Something went wrong");
		transactionInfo.setStatusCode(HttpStatus.SC_FORBIDDEN);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setStatus(HttpStatus.SC_FORBIDDEN);
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(transactionInfo));
		wrapper.getWriter().flush();
	}
}
