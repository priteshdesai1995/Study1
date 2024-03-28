package com.humaine.portal.api.rest.service;

public interface BaseEmailService {
	void sendEmail(String email, String subject, String body);
	
	void sendEmail(String[] emails, String subject, String body);
}
