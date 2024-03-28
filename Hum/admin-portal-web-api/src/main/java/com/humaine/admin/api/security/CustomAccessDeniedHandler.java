package com.humaine.admin.api.security;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.admin.api.util.ErrorField;
import com.humaine.admin.api.util.ErrorStatus;
import com.humaine.admin.api.util.TransactionInfo;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		ObjectMapper objMapper = new ObjectMapper();

		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(new ErrorField(String.valueOf(HttpStatus.SC_FORBIDDEN), "Something went wrong"));
		transactionInfo.setStatusCode(HttpStatus.SC_FORBIDDEN);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setStatus(HttpStatus.SC_FORBIDDEN);
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(transactionInfo));
		wrapper.getWriter().flush();
	}

}
