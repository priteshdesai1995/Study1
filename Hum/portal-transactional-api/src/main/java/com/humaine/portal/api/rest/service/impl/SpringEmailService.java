package com.humaine.portal.api.rest.service.impl;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.humaine.portal.api.exception.EmailClientException;
import com.humaine.portal.api.rest.service.BaseEmailService;

@Service("SpringEmail")
public class SpringEmailService implements BaseEmailService {

	private static final String CHAR_SET = "UTF-8";
	private final String sender;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	public SpringEmailService(@Value("${email.sender}") String sender) {
		this.sender = sender;
	}

	@Override
	public void sendEmail(String email, String subject, String body) {
		sendEmail(new String[] { email }, subject, body);
	}

	@Override
	public void sendEmail(String[] emails, String subject, String body) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					CHAR_SET);

			helper.setTo(emails);
			helper.setText(body, true);
			helper.setSubject(subject);
			helper.setFrom(sender);
			emailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
			throw new EmailClientException(e.getMessage(), e);
		}

	}
}
