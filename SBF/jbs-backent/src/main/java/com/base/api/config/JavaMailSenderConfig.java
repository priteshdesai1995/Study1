package com.base.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailSenderConfig {
	
	
	private static final int GMAIL_SMTP_PORT = 587;

    @Value("${email.sender.host}")
    private String host;

    @Value("${email.sender.user}")
    private String user;

    @Value("${email.sender.password}")
    private String password;

    @Value("${email.sender.debug}")
    private Boolean debug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Set up Gmail config
        mailSender.setHost(host);
        mailSender.setPort(GMAIL_SMTP_PORT);

        // Set up email config (using udeesa email)
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", debug);
        return mailSender;
    }
    
    
	
}
