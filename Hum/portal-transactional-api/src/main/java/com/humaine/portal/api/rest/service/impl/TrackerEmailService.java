package com.humaine.portal.api.rest.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.humaine.portal.api.model.AccountTracker;
import com.humaine.portal.api.rest.repository.AccountTrackerRepository;
import com.humaine.portal.api.rest.service.BaseEmailService;
import com.humaine.portal.api.util.DateUtils;
import com.humaine.portal.api.util.URLUtils;

@Service
public class TrackerEmailService {

	@Autowired
	@Qualifier("AwsEmail")
	private BaseEmailService emailService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${git.tracker.email.subject}")
	private String subject;

	@Value("${tracker.cdn.url}")
	private String trackerCDN;

	@Autowired
	private AccountTrackerRepository accountTrackerRepository;

	public void sendEmail(AccountTracker tracker) {
		Map<String, Object> props = new HashMap<>();
		props.put("link", URLUtils.getURL(trackerCDN) + tracker.getFilename());
		if (tracker.getAccount().getBusinessInformation()!=null) {			
			props.put("name", tracker.getAccount().getBusinessInformation().getName());
		}
		Context context = new Context();
		context.setVariables(props);

		String body = templateEngine.process("tracker-template", context);

		try {
			emailService.sendEmail(tracker.getEmail(), subject, body);
			tracker.setIsEmailSend(true);
			tracker.setModifiedOn(DateUtils.getCurrentTimestemp());
			tracker.setDescription(null);
			accountTrackerRepository.save(tracker);
		} catch (Exception e) {
			e.printStackTrace();
			tracker.setIsEmailSend(false);
			tracker.setDescription(e.getMessage());
			tracker.setModifiedOn(DateUtils.getCurrentTimestemp());
			accountTrackerRepository.save(tracker);
		}
	}
}
