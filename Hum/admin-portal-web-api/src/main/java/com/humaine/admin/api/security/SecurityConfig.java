package com.humaine.admin.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.humaine.admin.api.service.impl.UserServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${server.servlet.context-path:''}")
	private String contextPath;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	CommonBasicAuthenticationEntryPoint commonBasicAuthenticationEntryPoint;

	@Override
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected void configure(HttpSecurity http) throws Exception {
		http.anonymous().and().cors().and().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/actuator/**").permitAll().antMatchers("/routes/**").permitAll().antMatchers("/oauth/**")
				.permitAll().antMatchers("/user/**").permitAll()
//				.antMatchers("/api/**").authenticated()
				.anyRequest().authenticated().and().httpBasic().and().exceptionHandling()
				.authenticationEntryPoint(commonBasicAuthenticationEntryPoint);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> platformCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration configAutenticacao = new CorsConfiguration();
		configAutenticacao.setAllowCredentials(true);
		configAutenticacao.addAllowedOrigin("*");
		configAutenticacao.addAllowedHeader("X-Frame-Options");
		configAutenticacao.addAllowedHeader("Authorization");
		configAutenticacao.addAllowedHeader("Content-Type");
		configAutenticacao.addAllowedHeader("Accept");
		configAutenticacao.addAllowedHeader("*");
		configAutenticacao.addAllowedMethod("POST");
		configAutenticacao.addAllowedMethod("GET");
		configAutenticacao.addAllowedMethod("DELETE");
		configAutenticacao.addAllowedMethod("PUT");
		configAutenticacao.addAllowedMethod("OPTIONS");
		configAutenticacao.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", configAutenticacao);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(-110);
		return bean;
	}

	@Bean
	protected JwtAccessTokenConverter accessTokenConverter() {
		/*
		 * KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new
		 * ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
		 * JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		 * converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
		 */
		// -- for the simple demo purpose, used the secret for signing.
		// -- for production, it is recommended to use public/private key pair
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("Demo-Key-1");
		return converter;
	}

}
