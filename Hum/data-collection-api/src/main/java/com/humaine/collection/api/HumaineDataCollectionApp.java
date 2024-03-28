package com.humaine.collection.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.humaine.collection.api.config.AppConfiguration;
import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.util.ErrorMessageUtils;

@SpringBootApplication
@EnableScheduling
public class HumaineDataCollectionApp {

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	public static void main(String[] args) {
		SpringApplication.run(HumaineDataCollectionApp.class, args);
	}

	@PostConstruct
	public void setMessageCodeSpeperator() {
		AppConfiguration.messageCodeSpeperator = errorMessageUtils.getMessage("error.code.separator");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public RegistrationBean jwtAuthFilterRegister(CustomAuthenticationFilter filter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}
}
