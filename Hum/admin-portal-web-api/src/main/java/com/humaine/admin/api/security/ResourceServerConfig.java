package com.humaine.admin.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "common";

	@Value("${server.servlet.context-path:''}")
	private String contextPath;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(RESOURCE_ID).stateless(false)
				.authenticationEntryPoint(new CommonBasicAuthenticationEntryPoint())
				.accessDeniedHandler(new CustomAccessDeniedHandler());
	}

//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		// -- define URL patterns to enable OAuth2 security
//		http.anonymous().disable().requestMatchers().antMatchers("/api/**").and().authorizeRequests()
//				.antMatchers("/api/**").access("hasRole('ROLE_SUPERADMIN')");
//	}

}
