package com.base.api.security;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomTokenConverter extends JwtAccessTokenConverter {

	@Autowired
	private UserConfigMetaData userConfigMetaData;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		final Map<String, Object> additionalInfo = new HashMap<>();
		OAuth2AccessToken oAuth2AccessToken;
		additionalInfo.putAll(userConfigMetaData.getUserRelatedInformation(authentication.getName()));
		oAuth2AccessToken = super.enhance(accessToken, authentication);
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		oAuth2AccessToken.getAdditionalInformation().putAll(additionalInfo);
		return oAuth2AccessToken;
	}

}
