package com.humaine.portal.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.humaine.portal.api.config.AppConfiguration;
import com.humaine.portal.api.security.filter.AwsCognitoJwtAuthenticationFilter;
import com.humaine.portal.api.util.ErrorMessageUtils;

@SpringBootApplication
@EnableAsync
@ComponentScan(basePackages = "com.humaine.*")
public class PortalTransactionAPIApp {

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	public static void main(String[] args) {
		SpringApplication.run(PortalTransactionAPIApp.class, args);
	}

	@PostConstruct
	public void setMessageCodeSpeperator() {
		AppConfiguration.messageCodeSpeperator = errorMessageUtils.getMessage("error.code.separator");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RegistrationBean jwtAuthFilterRegister(AwsCognitoJwtAuthenticationFilter filter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

//	@Bean
//	public TaskExecutor threadPoolTaskExecutor() {
//
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(4);
//		executor.setMaxPoolSize(10);
//		executor.setThreadNamePrefix("task_executor_thread-");
//		executor.initialize();
//
//		return executor;
//	}

}
