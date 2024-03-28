package com.humaine.transactional;

import java.util.Map;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.humaine.transactional.scheduler.Scheduler;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
@EnableRetry
public class LambdaFunctionHandler implements RequestHandler<Map<Object, Object>, Object> {

	private ApplicationContext getApplicationContext(String[] args) {
		return new SpringApplicationBuilder(LambdaFunctionHandler.class).run(args);
	}

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public String handleRequest(Map<Object, Object> event, Context context) {
		ApplicationContext ctx = getApplicationContext(new String[] {});
		Scheduler scheduler = ctx.getBean(Scheduler.class);
		LambdaLogger logger = context.getLogger();
		String response = new String("200 OK");
		logger.log("Humaine AI CI/CD pipeline test.");
		logger.log("--------------------- Schedular checking start --------------------- ");
		Long timestamp = System.currentTimeMillis();
		scheduler.userEventArchiveScheduler(timestamp);
		scheduler.userSessionArchiveScheduler(timestamp);
		scheduler.saleDataArchiveScheduler(timestamp);
		logger.log("--------------------- Schedular checking end --------------------- --");
		return response;
	}
}