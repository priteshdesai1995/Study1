	package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
//	@Bean
//	@Override
//	protected UserDetailsService userDetailsService() {
//		List<UserDetails> users = new ArrayList<>();
//		users.add(User.withDefaultPasswordEncoder().username("pritesh").password("pritesh").roles("USER").build());
//		return new InMemoryUserDetailsManager(users);
//	}

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//		daoAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return daoAuthenticationProvider;
	}

	// for adding the custom JSP for the login and logout
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable().authorizeHttpRequests().antMatchers("/login").permitAll().anyRequest().authenticated()
//				.and().formLogin().loginPage("/login").permitAll().and().logout().invalidateHttpSession(true)
//				.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//				.logoutSuccessUrl("/logout-success").permitAll();
		
		http.csrf().disable().authorizeHttpRequests().antMatchers("/login").permitAll().anyRequest().authenticated()
		.and().httpBasic();
	}

}
