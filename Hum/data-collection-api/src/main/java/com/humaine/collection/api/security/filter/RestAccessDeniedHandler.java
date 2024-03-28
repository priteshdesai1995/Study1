package com.humaine.collection.api.security.filter;

import java.io.IOException;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humaine.collection.api.util.ErrorField;
import com.humaine.collection.api.util.ErrorStatus;
import com.humaine.collection.api.util.TransactionInfo;

/**
 * Handles the exception for the forbidden requests.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		ObjectMapper objMapper = new ObjectMapper();

		TransactionInfo transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
		transactionInfo.addErrorList(
				new ErrorField(accessDeniedException.getMessage(), String.valueOf(HttpStatus.SC_FORBIDDEN)));
		transactionInfo.setStatusCode(HttpStatus.SC_FORBIDDEN);

		final HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.setStatus(HttpStatus.SC_FORBIDDEN);
		wrapper.setContentType(APPLICATION_JSON_VALUE);
		wrapper.getWriter().println(objMapper.writeValueAsString(transactionInfo));
		wrapper.getWriter().flush();

	}
}
