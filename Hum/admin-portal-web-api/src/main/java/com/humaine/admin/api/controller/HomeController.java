package com.humaine.admin.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.admin.api.model.AccountStatus;
import com.humaine.admin.api.repository.AccountRepository;
import com.humaine.admin.api.util.ResponseBuilder;
import com.humaine.admin.api.util.TransactionInfo;

@RestController
@RequestMapping("/home")
public class HomeController {

	@Autowired
	AccountRepository accountRepository;

	private static final Logger log = LogManager.getLogger(CustomerManagementController.class);

	@GetMapping(value = "/getRegisteredCustomers", headers = {
			"Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionInfo> getCountOfRegisteredCustomers() {
		log.info("Get All Registered Customers call starts... ");
		Long count = accountRepository.getCountOfRegisteredCustomers(AccountStatus.confirmed.value());
		log.info("Get All Registered Customers call ends... ");
		return ResponseBuilder.buildResponse(count);
	}

}
