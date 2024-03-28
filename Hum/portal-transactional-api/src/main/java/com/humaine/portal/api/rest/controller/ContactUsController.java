package com.humaine.portal.api.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.request.dto.ContactUsRequest;
import com.humaine.portal.api.rest.olap.service.impl.ContactUsEmailService;
import com.humaine.portal.api.util.ErrorMessageUtils;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Front Portal APIs", description = "APIs for Front Portal")
@RequestMapping("/front/portal")
public class ContactUsController {

	@Autowired
	private ContactUsEmailService contactUsEmailService;
	
	@Autowired
	private ErrorMessageUtils errorMessageUtils;
	
	@PostMapping(value = "contact", headers = {
			"version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Contact US", notes = "Contact US")
	public ResponseEntity<TransactionInfo> contactUs(@Valid @RequestBody ContactUsRequest request) {
		contactUsEmailService.sendEmail(request);
		return ResponseBuilder.buildMessageCodeResponse(errorMessageUtils
				.getMessageWithCode("api.success.contact.us", null, "api.success.contact.us.code"));
	}

}
