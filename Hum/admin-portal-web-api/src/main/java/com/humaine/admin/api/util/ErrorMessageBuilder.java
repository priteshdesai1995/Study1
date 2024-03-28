package com.humaine.admin.api.util;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.humaine.admin.api.config.AppConfiguration;

public class ErrorMessageBuilder {

	public static String getMessageWithCode(String msg) {
		return getMessageWithCode(msg, null);
	}

	public static String getMessageWithCode(String msg, String code) {
		ArrayList<String> result = new ArrayList<String>();
		if (!StringUtils.isBlank(msg))
			result.add(msg);
		if (!StringUtils.isBlank(code))
			result.add(code);
		return String.join(AppConfiguration.messageCodeSpeperator, result);
	}

	public static String retriveMessage(String msg) {
		String[] result = processMessage(msg);
		if (result.length == 0)
			return "";
		return result[0];
	}

	public static String retriveCode(String msg) {
		return retriveCode(msg, null);
	}

	public static String retriveCode(String msg, String defaultValue) {
		if (StringUtils.isBlank(defaultValue))
			defaultValue = "";
		String[] result = processMessage(msg);
		if (result.length <= 1)
			return defaultValue;
		return result[1];
	}

	private static String[] processMessage(String msg) {
		if (StringUtils.isBlank(msg))
			msg = "";
		String[] splited = msg.split(AppConfiguration.messageCodeSpeperator);
		return splited;
	}
}
