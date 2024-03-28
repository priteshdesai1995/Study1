package com.base.api.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.base.api.constants.Constants;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class TimezoneConfig {

	@Value("${application.timezone:"+Constants.DEFAULT_SERVER_TIMEZONE+"}")
	String applicationTimezone;

	@PostConstruct
	void started() {
		log.info("===============Set Application Timezone to: {} ===========", applicationTimezone);
		TimeZone.setDefault(TimeZone.getTimeZone(applicationTimezone));
	}
}
