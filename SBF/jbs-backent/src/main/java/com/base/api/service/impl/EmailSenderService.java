package com.base.api.service.impl;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.base.api.request.dto.Mail;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSenderService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	/**
	 * Send email.
	 *
	 * @param mail the mail
	 * @return the string
	 */
	public String sendEmail(Mail mail) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			String html = getHtmlContent(mail);

			helper.setTo(mail.getTo());
			helper.setFrom(mail.getFrom());
			helper.setSubject(mail.getSubject());
			helper.setText(html, true);
			emailSender.send(message);
			return HttpStatus.OK.name();
		} catch (MessagingException ex) {
			log.error(ex.getMessage());
			return ex.getMessage();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return ex.getMessage();
		}
	}

	/**
	 * Gets the html content.
	 *
	 * @param mail the mail
	 * @return the html content
	 */
	private String getHtmlContent(Mail mail) {
		Context context = new Context();
		context.setVariables(mail.getHtmlTemplate().getProps());
		return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
	}
}
