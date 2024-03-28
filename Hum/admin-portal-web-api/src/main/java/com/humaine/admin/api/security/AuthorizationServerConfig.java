package com.humaine.admin.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.humaine.admin.api.service.impl.UserServiceImpl;
import com.humaine.admin.api.util.SecurityConstants;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private static String REALM = "COMMON_REALM";

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	UserServiceImpl userServiceImpl;

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	private static final String RESOURCE_ID = "common";

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(SecurityConstants.CLIENT_ID).secret(SecurityConstants.CLIENT_SECRET)
				.authorizedGrantTypes(SecurityConstants.GRANT_TYPE_PASSWORD, SecurityConstants.AUTHORIZATION_CODE,
						SecurityConstants.REFRESH_TOKEN, SecurityConstants.IMPLICIT)
				.scopes(SecurityConstants.SCOPE_READ, SecurityConstants.SCOPE_WRITE, SecurityConstants.TRUST)
				.resourceIds(RESOURCE_ID).accessTokenValiditySeconds(SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS)
				.refreshTokenValiditySeconds(SecurityConstants.REFRESH_TOKEN_VALIDITY_SECONDS);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.tokenStore(tokenStore()).tokenEnhancer(customTokenEnhancer()).userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager).userDetailsService(userServiceImpl);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.realm(REALM);
		oauthServer.checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
	}

	@Bean
	public CustomTokenConverter customTokenEnhancer() {
		return new CustomTokenConverter();
	}

//	@Bean
//	@Primary
//	public DefaultTokenServices tokenServices() {
//		DefaultTokenServices tokenServices = new DefaultTokenServices();
//		tokenServices.setSupportRefreshToken(true);
//		tokenServices.setTokenStore(tokenStore());
//		return tokenServices;
//	}

}
