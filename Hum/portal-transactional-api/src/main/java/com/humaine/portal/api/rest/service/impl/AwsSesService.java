package com.humaine.portal.api.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.humaine.portal.api.exception.EmailClientException;
import com.humaine.portal.api.rest.service.BaseEmailService;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;

@Service("AwsEmail")
public class AwsSesService implements BaseEmailService {

	private static final String CHAR_SET = "UTF-8";
	private final String sender;

	@Autowired

	private AWSClientProviderBuilder builder;

	@Autowired
	public AwsSesService(@Value("${email.sender}") String sender) {
		this.sender = sender;
	}

	public void sendEmail(String email, String subject, String body) {
		sendEmail(new String[]{email}, subject, body);
	}

	@Override
	public void sendEmail(String[] emails, String subject, String body) {
		try {
			int requestTimeout = 3000;
			SendEmailRequest request = new SendEmailRequest().withDestination(new Destination().withToAddresses(emails))
					.withMessage(new Message()
							.withBody(new Body().withHtml(new Content().withCharset(CHAR_SET).withData(body)))
							.withSubject(new Content().withCharset(CHAR_SET).withData(subject)))
					.withSource(sender).withSdkRequestTimeout(requestTimeout);
			builder.getAmazonSimpleEmailServiceClient().sendEmail(request);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new EmailClientException(e.getMessage(), e);
		}
	}
}
