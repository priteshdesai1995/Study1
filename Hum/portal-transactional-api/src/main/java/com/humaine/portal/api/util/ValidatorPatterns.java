package com.humaine.portal.api.util;

public class ValidatorPatterns {
	public static final String strongPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	public static final String otp = "^[0-9]{6}$";
}
