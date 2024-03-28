package com.humaine.transactional.jobs;

import java.time.OffsetDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.humaine.transactional.enums.LoopStatus;
import com.humaine.transactional.repository.UserSessionReaderRepository;

public class UserSessionLoopDecider implements JobExecutionDecider {

	private static final Logger logger = LogManager.getLogger(UserSessionLoopDecider.class);

	private UserSessionReaderRepository userSessionReaderRepository;

	private OffsetDateTime timestemp;

	public UserSessionLoopDecider(UserSessionReaderRepository userSessionReaderRepository, OffsetDateTime timestemp) {
		super();
		this.userSessionReaderRepository = userSessionReaderRepository;
		this.timestemp = timestemp;
	}

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		Long count = this.userSessionReaderRepository.getArchiveRecordsCount(timestemp);
		
		if (count == 0L) {
			logger.info("UserSession Archive Job::count:: {}", count, LoopStatus.COMPLETED.value());
			return new FlowExecutionStatus(LoopStatus.COMPLETED.value());
		}
		logger.info("UserSession Archive Job::count:: {}", count, LoopStatus.CONTINUE.value());
		return new FlowExecutionStatus(LoopStatus.CONTINUE.value());
	}

}
