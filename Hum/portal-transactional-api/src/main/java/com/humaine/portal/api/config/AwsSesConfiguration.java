package com.humaine.portal.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.humaine.portal.api.security.config.AWSConfig;

@Configuration
@Import(AWSConfig.class)
public class AwsSesConfiguration {

	@Autowired(required = true)
	private AWSConfig awsConfig;
	
//	@Bean
//	public AmazonSimpleEmailService amazonSimpleEmailService() {
//		return AmazonSimpleEmailServiceClientBuilder.standard()
//				.withRegion(awsConfig.getRegion())
//				.build();
//	}
}
