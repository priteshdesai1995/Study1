package com.humaine.portal.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {
	@Autowired
	private Environment env;

	@Value("${email.sender}")
	private String sender;
	
	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

//        mailSender.setHost(env.getProperty("spring.mail.host"));
//        mailSender.setPort(Integer.valueOf(env.getProperty("spring.mail.port")));
//        mailSender.setUsername(env.getProperty("spring.mail.username"));
//        mailSender.setPassword(env.getProperty("spring.mail.password"));
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername(sender);
		mailSender.setPassword("");

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "false");
		javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

}
