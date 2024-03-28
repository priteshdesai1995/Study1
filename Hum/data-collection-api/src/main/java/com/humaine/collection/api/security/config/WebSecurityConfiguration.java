package com.humaine.collection.api.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.humaine.collection.api.security.filter.CustomAuthenticationFilter;
import com.humaine.collection.api.security.filter.RestAccessDeniedHandler;
import com.humaine.collection.api.security.filter.SecurityAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] publicPaths = { "/sessionTimeout/test", "/actuator/**" };

	public static final String[] securePathWithOutCheckingAccountRegistrationStatus = { "/register" };

	@Autowired
	private CustomAuthenticationFilter authenticationFilter;

	public WebSecurityConfiguration() {
		super(true);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.eraseCredentials(false);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(publicPaths);
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
				"/configuration/security", "/swagger-ui.html", "/webjars/**", "/eventRecording/**");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
				.accessDeniedHandler(new RestAccessDeniedHandler()).and().anonymous().and()
				/* No Http session is used to get the security context */
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers(publicPaths).permitAll().anyRequest().authenticated().and()
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
