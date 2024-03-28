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
import com.humaine.transactional.jobs.PageLoadDataJob;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
@EnableRetry
public class PageLoadDataLambdaFunctionHandler implements RequestHandler<Map<Object, Object>, Object> {

	private ApplicationContext getApplicationContext(String[] args) {
		return new SpringApplicationBuilder(PageLoadDataLambdaFunctionHandler.class).run(args);
	}

	@Override
	public String handleRequest(Map<Object, Object> input, Context context) {
		ApplicationContext ctx = getApplicationContext(new String[] {});
		PageLoadDataJob pageLoadData = ctx.getBean(PageLoadDataJob.class);
		LambdaLogger logger = context.getLogger();
		String response = new String("200 OK");
		logger.log("---------------------  Page Load Data Delete start --------------------- ");
		int total = pageLoadData.deleteOlderSchedular();
		logger.log("Total Page Load Data Records Deleted --- {" + total + "}");
		logger.log("--------------------- Page Load Data Delete end --------------------- --");
		return response;
	}

}
