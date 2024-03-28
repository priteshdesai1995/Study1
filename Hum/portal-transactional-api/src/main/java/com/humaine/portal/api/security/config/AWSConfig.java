package com.humaine.portal.api.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource("classpath:aws-cognito.properties")
@PropertySource("classpath:AwsCredentials.properties")
@Getter
@Setter
@ConfigurationProperties
public class AWSConfig {
	private static final String COGNITO_IDENTITY_POOL_URL = "https://cognito-idp.%s.amazonaws.com/%s";
	private static final String JSON_WEB_TOKEN_SET_URL_SUFFIX = "/.well-known/jwks.json";

	private String clientId;
	private String poolId;
	private String endpoint;
	private String region;
	private String identityPoolId;
	private String developerGroup;
	
	private String accessKey;
	private String secretKey;
	
	private String heatmapAccessKey;
	private String heatmapSecretKey;
	private String heatmapRegion;

	private String userNameField = "cognito:username";
	private String groupsField = "cognito:groups";
	private String emailField = "email";
	private String emailVerifiedField = "email_verified";
	private String customUsername = "custom:username";
	private int connectionTimeout = 2000;
	private int readTimeout = 2000;
	public static final String header =  "Authorization";
	private String httpHeader = header;

	public String getJwkUrl() {
		StringBuilder cognitoURL = new StringBuilder();
		cognitoURL.append(COGNITO_IDENTITY_POOL_URL);
		cognitoURL.append(JSON_WEB_TOKEN_SET_URL_SUFFIX);
		return String.format(cognitoURL.toString(), region, poolId);
	}

	public String getCognitoIdentityPoolUrl() {
		return String.format(COGNITO_IDENTITY_POOL_URL, region, poolId);
	}
}
