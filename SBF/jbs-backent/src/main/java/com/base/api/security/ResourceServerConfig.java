package com.base.api.security;

import static com.base.api.constants.RoleConstants.ROLE_SUPER_ADMIN;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "common";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// -- define URL patterns to enable OAuth2 security
		http.anonymous().disable().requestMatchers().antMatchers("/**").and().authorizeRequests()
				.antMatchers("/**")
				.access("hasRole('"+ROLE_SUPER_ADMIN+"') or hasRole('"+ROLE_SUPER_ADMIN+"')").and()
				.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());

	}
}
