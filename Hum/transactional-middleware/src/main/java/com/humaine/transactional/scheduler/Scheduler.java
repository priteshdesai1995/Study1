package com.humaine.transactional.scheduler;

import java.util.HashMap;
import java.util.Map;

import javax.batch.operations.JobRestartException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.humaine.transactional.jobs.SaleDataJob;
import com.humaine.transactional.jobs.UserEventJob;
import com.humaine.transactional.jobs.UserSessionJob;

@Component
//@EnableScheduling
public class Scheduler {

	private static final Logger logger = LogManager.getLogger(Scheduler.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private UserEventJob eventJob;

	@Autowired
	private UserSessionJob userSessionJob;

	@Autowired
	private SaleDataJob saleDataJob;

	// @Scheduled(cron = "0 0 23 28-31 * ?")
	public void userEventArchiveScheduler(Long timestamp) {
		logger.info("User Event Archive Scheduler Start");
		Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			JobExecution ex = jobLauncher.run(eventJob.job(timestamp), jobParameters);

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error("User Event Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("User Event Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("User Event Archive Scheduler end");
	}

	// @Scheduled(cron = "0 0 23 28-31 * ?")
	public void userSessionArchiveScheduler(Long timestamp) {
		logger.info("User Session Archive Scheduler Start");
		Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			JobExecution ex = jobLauncher.run(userSessionJob.sessionJob(timestamp), jobParameters);

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error("User Session Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("User Session Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("User Session Archive Scheduler end");
	}

	// @Scheduled(cron = "0 0 23 28-31 * ?")
	public void saleDataArchiveScheduler(Long timestamp) {
		logger.info("Sale Data Archive Scheduler Start");
		Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			JobExecution ex = jobLauncher.run(saleDataJob.saleJob(timestamp), jobParameters);

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error("Sale Data Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Sale Data Archive Scheduler: Error:" + e.getMessage());
			e.printStackTrace();
		}
		logger.info("Sale Data Archive Scheduler End");
	}
}