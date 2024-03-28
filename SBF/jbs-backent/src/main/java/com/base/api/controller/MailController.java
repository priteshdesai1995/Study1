package com.base.api.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.request.dto.MailRequest;
import com.base.api.service.impl.SpringEmailService;

@RestController
public class MailController {
	/*
	 * @Value("${email.sender}") private String toMail;
	 */

	
	@Autowired
	private SpringEmailService emailService;

	@RequestMapping(value = "/email", method = RequestMethod.POST)
	@ResponseBody
	public String sendMail(@RequestBody MailRequest mailrequest) throws MessagingException {
		emailService.sendMail(mailrequest);
		return "Email Sent Successfully.!";

	}
}