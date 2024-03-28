package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Service.CustomerDetailService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomerDetailService customerDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/customer/*").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin();
			//.httpBasic();
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("swapnil").password("123456")
//				.roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("Devendra").password("12345")
//				.roles("NORMAL");
//	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("swapnil").password(this.passwordEncoder().encode("123456"))
//				.roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("Devendra").password(this.passwordEncoder().encode("12345"))
//				.roles("NORMAL");
//	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();	
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customerDetailService).passwordEncoder(passwordEncoder());
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

}
