package com.humaine.portal.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.portal.api.model.AccountTracker;
import com.humaine.portal.api.rest.repository.AccountTrackerRepository;
import com.humaine.portal.api.rest.service.impl.TrackerEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "trackernotification", description = "Send Generated Tracker to Business on Mail")
@RequestMapping("trackernotification")
@Transactional(propagation = Propagation.NEVER)
public class TrackerNotification {

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TrackerEmailService emailService;

	@Autowired
	private AccountTrackerRepository accountTrackerRepository;

	@GetMapping(value = "", headers = { "version=v1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Send Generated Tracker to Business on Mail", notes = "Send Generated Tracker to Business on Mail")
	@Transactional(propagation = Propagation.NEVER)
	public ResponseEntity<String> sendTrackerOnMail() {

		List<AccountTracker> trackers = accountTrackerRepository.getPendingAccountTracker();
		if (trackers != null) {
			for (AccountTracker tracker : trackers) {
				send(tracker);
			}
		}
		return new ResponseEntity<String>("Email Notification Process initiated", HttpStatus.OK);
	}

	public void send(AccountTracker tracker) {
		taskExecutor.execute(new EmailSendRunnable(tracker));
	}

	@Transactional(propagation = Propagation.NEVER)
	private class EmailSendRunnable implements Runnable {

		AccountTracker tracker;

		public EmailSendRunnable(AccountTracker tracker) {
			this.tracker = tracker;
		}

		@Override
		public void run() {
			emailService.sendEmail(tracker);
		}

	}
}
