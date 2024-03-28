package com.humaine.transactional;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humaine.transactional.jobs.UserEventJob;

@RestController
@RequestMapping("test")
public class TestContrller {
	private static final Logger logger = LogManager.getLogger(TestContrller.class);
	@Autowired
	private UserEventJob eventJob;

	@Autowired
	private JobLauncher jobLauncher;

	@GetMapping
	public String userEventArchiveScheduler() {
		logger.info("User Event Archive Scheduler Start");
		Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
		JobParameters jobParameters = new JobParameters(confMap);
		try {
			Long timestamp = System.currentTimeMillis();
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
		return "done";
	}
}
