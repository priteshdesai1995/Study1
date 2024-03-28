package com.humaine.portal.api.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.humaine.portal.api.request.dto.SignInRequest;
import com.humaine.portal.api.security.model.SpringSecurityUser;
import com.humaine.portal.api.security.service.CognitoAuthenticationService;
import com.humaine.portal.api.util.Constants;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	CognitoAuthenticationService cognitoService;

	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) {
		SignInRequest authenticationRequest = null;

		if (null != authentication) {
			authenticationRequest = new SignInRequest();
			Map<String, String> credentials = (Map<String, String>) authentication.getCredentials();
			authenticationRequest.setPassword(credentials.get(Constants.PASS_WORD_KEY));
			authenticationRequest.setUsername(authentication.getName());

			SpringSecurityUser userAuthenticated = null;
			try {
				userAuthenticated = cognitoService.authenticate(authenticationRequest);
			} catch (Exception e) {
				throw new UsernameNotFoundException(e.getMessage());
			}
			if (null != userAuthenticated) {
				// use the credentials
				// and authenticate against the third-party system

				Map<String, String> authenticatedCredentials = new HashMap<>();
				authenticatedCredentials.put(Constants.ACCESS_TOKEN_KEY, userAuthenticated.getAccessToken());
				authenticatedCredentials.put(Constants.EXPIRES_IN_KEY, userAuthenticated.getExpiresIn().toString());
				authenticatedCredentials.put(Constants.ID_TOKEN_KEY, userAuthenticated.getIdToken());
				authenticatedCredentials.put(Constants.PASS_WORD_KEY, userAuthenticated.getPassword());
				authenticatedCredentials.put(Constants.REFRESH_TOKEN_KEY, userAuthenticated.getRefreshToken());
				authenticatedCredentials.put(Constants.TOKEN_TYPE_KEY, userAuthenticated.getTokenType());
				return new UsernamePasswordAuthenticationToken(userAuthenticated.getUsername(),
						authenticatedCredentials, userAuthenticated.getAuthorities());
			} else {
				return null;
			}
		} else {
			throw new UsernameNotFoundException(String.format("No appUser found with username '%s'.", ""));
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
