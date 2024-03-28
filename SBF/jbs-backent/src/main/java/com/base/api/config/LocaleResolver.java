package com.base.api.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.base.api.constants.Constants;

@Configuration
public class LocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

	static final List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("fr"));

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String headerLang = request.getHeader(Constants.LANGUAGE_HEADER);
		return headerLang == null || headerLang.isEmpty() ? new Locale(Constants.DEFAULT_LOCALE)
				: Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
	}

	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
