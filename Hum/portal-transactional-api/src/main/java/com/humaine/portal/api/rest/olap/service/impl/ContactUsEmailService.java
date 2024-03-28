package com.humaine.portal.api.rest.olap.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.humaine.portal.api.exception.APIException;
import com.humaine.portal.api.request.dto.ContactUsRequest;
import com.humaine.portal.api.rest.service.BaseEmailService;
import com.humaine.portal.api.util.ErrorMessageUtils;

@Service
public class ContactUsEmailService {

	private static final Logger log = LogManager.getLogger(ContactUsEmailService.class);

	@Autowired
	@Qualifier("AwsEmail")
	private BaseEmailService emailService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${contact.us.email.subject}")
	private String subject;

	@Value("${contact.us.email.recipients}")
	private String recipients;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	public void sendEmail(ContactUsRequest request) {
		log.info("Contact Us Request with Email:{}", request.getEmail());
		Map<String, Object> props = new HashMap<>();
		Map<String, Object> contactData = new HashMap<>();
		contactData.put("Full Name", request.getFullName());
		contactData.put("Email", request.getEmail());
		contactData.put("Phone Number", request.getPhoneNumber());
		if (!StringUtils.isBlank(request.getMessage())) {
			contactData.put("Message", request.getMessage());
		}

		props.put("contactData", contactData);

		Context context = new Context();
		context.setVariables(props);

		String body = templateEngine.process("contact-us-template", context);
		try {
			emailService.sendEmail(getRecipientsList(), subject, body);
		} catch (Exception e) {
			log.error("Error While Sending Contact Us Email: => Eror: ${}", e.getMessage());
			throw new APIException(
					errorMessageUtils.getMessageWithCode("api.error.contact.us.not.able.to.process.request", null,
							"api.error.contact.us.not.able.to.process.request.code"));
		}
	}

	private String[] getRecipientsList() {
		if (StringUtils.isBlank(recipients)) {
			throw new APIException("No Recipients Found");
		}
		return recipients.split(",");
	}

}
