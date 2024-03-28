package com.humaine.portal.api.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.humaine.portal.api.security.config.AWSConfig;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AWSClientProviderBuilder {

	@Autowired
	private AWSConfig cognitoConfig;

	private AWSCognitoIdentityProvider cognitoIdentityProvider;
	private AmazonSimpleEmailService amazonSimpleEmailService;
	private AmazonS3 s3Client;
	private AWSStaticCredentialsProvider propertiesFileCredentialsProvider;
	private AWSStaticCredentialsProvider propertiesFileS3CredentialsProvider;
	private String s3region;
	private String region;

	private void initCommonInfo() {
		if (null == propertiesFileCredentialsProvider) {
			BasicAWSCredentials credentials = new BasicAWSCredentials(cognitoConfig.getAccessKey(),
					cognitoConfig.getSecretKey());
			propertiesFileCredentialsProvider = new AWSStaticCredentialsProvider(credentials);
		}
		if (null == region) {
			region = cognitoConfig.getRegion();
		}
	}
	
	private void initS3Info() {
		if (null == propertiesFileS3CredentialsProvider) {
			BasicAWSCredentials credentials = new BasicAWSCredentials(cognitoConfig.getHeatmapAccessKey(),
					cognitoConfig.getHeatmapSecretKey());
			propertiesFileS3CredentialsProvider = new AWSStaticCredentialsProvider(credentials);
		}
		if (null == region) {
			s3region = cognitoConfig.getHeatmapRegion();
		}
	}

	public AmazonS3 getAWSS3Client() {
		if (null == s3Client) {
			initS3Info();

			s3Client = AmazonS3ClientBuilder.standard()
					.withCredentials(propertiesFileS3CredentialsProvider).withRegion(s3region).build();
		}
		return s3Client;
	}

	public AWSCognitoIdentityProvider getAWSCognitoIdentityClient() {
		if (null == cognitoIdentityProvider) {
			initCommonInfo();

			cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
					.withCredentials(propertiesFileCredentialsProvider).withRegion(region).build();
		}

		return cognitoIdentityProvider;
	}

	public AmazonSimpleEmailService getAmazonSimpleEmailServiceClient() {
		if (null == amazonSimpleEmailService) {
			initCommonInfo();
			amazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder.standard()
					.withCredentials(propertiesFileCredentialsProvider).withRegion(cognitoConfig.getRegion()).build();
		}
		return amazonSimpleEmailService;
	}
}
