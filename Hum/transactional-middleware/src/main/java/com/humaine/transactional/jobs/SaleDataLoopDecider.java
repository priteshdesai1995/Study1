package com.humaine.transactional.jobs;

import java.time.OffsetDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.humaine.transactional.enums.LoopStatus;
import com.humaine.transactional.repository.SaleDataReaderRepository;

public class SaleDataLoopDecider implements JobExecutionDecider {

	private static final Logger logger = LogManager.getLogger(SaleDataLoopDecider.class);
	
	private SaleDataReaderRepository saleDataReaderRepository;

	private OffsetDateTime timestemp;

	public SaleDataLoopDecider(SaleDataReaderRepository saleDataReaderRepository, OffsetDateTime timestemp) {
		super();
		this.saleDataReaderRepository = saleDataReaderRepository;
		this.timestemp = timestemp;
	}

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		Long count = this.saleDataReaderRepository.getArchiveRecordsCount(timestemp);
		if (count == 0L) {
			logger.info("SaleData Archive Job::count:: {}, {}" ,count, LoopStatus.COMPLETED.value());
			return new FlowExecutionStatus(LoopStatus.COMPLETED.value());
		}
		logger.info("SaleData Archive Job::count:: {}, {}" ,count, LoopStatus.CONTINUE.value());
		return new FlowExecutionStatus(LoopStatus.CONTINUE.value());
	}

}
