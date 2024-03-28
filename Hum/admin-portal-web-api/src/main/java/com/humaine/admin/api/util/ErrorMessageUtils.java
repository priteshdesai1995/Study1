package com.humaine.admin.api.util;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.humaine.admin.api.config.AppConfiguration;

@Component
public class ErrorMessageUtils {

	@Autowired
	private MessageSource messageSource;

	private Locale defaultLocale = Locale.US;

	public String getMessage(String path) {
		return getMessage(path, defaultLocale);
	}

	public String getMessage(String path, Locale locale) {
		return this.getMessage(path, null, locale);
	}

	public String getMessage(String path, Object[] params) {
		return this.getMessage(path, params, defaultLocale);
	}

	public String getMessage(String path, Object[] params, Locale locale) {
		return this.messageSource.getMessage(path, params, locale);
	}

	public String getMessageWithCode(String msg, Object[] msgParams, String code) {
		ArrayList<String> result = new ArrayList<String>();
		String message = getMessage(msg, msgParams, defaultLocale);
		String codeMessage = getMessage(code, defaultLocale);
		if (!StringUtils.isBlank(message))
			result.add(message);
		if (!StringUtils.isBlank(codeMessage))
			result.add(codeMessage);
		return String.join(AppConfiguration.messageCodeSpeperator, result);
	}
}
