package com.base.api.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Translator {
	private static MessageSource messageSource;

	@Autowired
	Translator(MessageSource messageSource) {
		Translator.messageSource = messageSource;
	}

	public static String toLocale(String msgCode) {
		return getMessageString(msgCode, null);
	}

	public static String toLocale(String msgCode, Object[] args) {
		return getMessageString(msgCode, args);
	}

	private static String getMessageString(String msgCode, Object[] args) {
		if (StringUtils.isEmpty(msgCode)) {
			msgCode = "";
		}
		String result = msgCode;
		Locale locale = LocaleContextHolder.getLocale();
		try {
			result = messageSource.getMessage(msgCode, args, locale);
		} catch (Exception e) {
			result = msgCode;
		}
		return result;
	}
}
