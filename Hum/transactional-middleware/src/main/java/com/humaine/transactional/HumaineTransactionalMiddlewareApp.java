package com.humaine.transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableScheduling
//@EnableBatchProcessing
//@EnableRetry
public class HumaineTransactionalMiddlewareApp implements ApplicationRunner {

	private static final Logger logger = LogManager.getLogger(HumaineTransactionalMiddlewareApp.class);

	public static void main(String[] args) {
		SpringApplication.run(HumaineTransactionalMiddlewareApp.class, args);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
	}
}
