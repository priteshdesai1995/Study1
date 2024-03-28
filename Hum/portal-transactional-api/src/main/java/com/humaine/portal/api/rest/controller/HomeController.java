package com.humaine.portal.api.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.enums.AccountStatus;
import com.humaine.portal.api.rest.repository.AccountAdminRepository;
import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.util.ResponseBuilder;
import com.humaine.portal.api.util.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	AccountAdminRepository accountRepository;

	private static final Logger log = LogManager.getLogger(CustomerManagementController.class);

	@GetMapping(value = "/getRegisteredCustomers", headers = {
			"Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Count of Registered Customers", notes = "Get AI User Group list", authorizations = {
			@Authorization(value = AWSConfig.header) })
	public ResponseEntity<TransactionInfo> getCountOfRegisteredCustomers() {
		log.info("Get All Registered Customers call starts... ");
		Long count = accountRepository.getCountOfRegisteredCustomers(AccountStatus.confirmed.value());
		log.info("Get All Registered Customers call ends... ");
		return ResponseBuilder.buildResponse(count);
	}

}
