package com.base.api.service.impl;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.base.api.request.dto.MailRequest;

@Service
public class SpringEmailService {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	public SpringEmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
		this.templateEngine = templateEngine;
		this.javaMailSender = javaMailSender;
	}

	public String sendMail(MailRequest mailRequest) throws MessagingException {
		
		  Context context = new Context(); 
		  context.setVariable("mailRequest",
		  mailRequest);
		  
		 
		String process = templateEngine.process("mail/email-template", context);
		javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject("Welcome " + (mailRequest.getName()));
		helper.setText(process, true);
		helper.setTo(mailRequest.getTo());
		javaMailSender.send(mimeMessage);
		return "Sent";
	}
	
	
	public String sendMail(MailRequest mailRequest,String[] to,String[] cc,String Bcc) throws MessagingException {
		
		  Context context = new Context(); 
		  context.setVariable("mailRequest",mailRequest);
		  
		 
		String process = templateEngine.process("mail/email-template", context);
		javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		helper.setSubject("Welcome " + mailRequest.getName());
		helper.setText(process, true);
		helper.setTo(to);
		helper.setCc(cc);
		helper.setBcc(Bcc);
		javaMailSender.send(mimeMessage);
		return "Sent";
	}
}