package com.humaine.portal.api.rest.repository.impl;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.humaine.portal.api.security.authentication.AWSClientProviderBuilder;

@Repository
public class AmazonS3Repository {

	@Autowired
	AWSClientProviderBuilder s3client;
	
	@Value("${s3.heatmap.pre-signed.url.expiration.time}")
	int signedUrlExpirationTime;
	
	@Value("${s3.heatmap.bucket.name}")
	String bucketName;
	
	public String getPreSignedUrl(String fileName) {

	    java.util.Date expiration = new java.util.Date();
	    long expTimeMillis = expiration.getTime();
	    expTimeMillis += 1000 * 60 * signedUrlExpirationTime; 
	    expiration.setTime(expTimeMillis);
	    GeneratePresignedUrlRequest generatePresignedUrlRequest =
	            new GeneratePresignedUrlRequest(bucketName, fileName)
	                    .withMethod(HttpMethod.GET)
	                    .withExpiration(expiration);
	    URL url = s3client.getAWSS3Client().generatePresignedUrl(generatePresignedUrlRequest);
	    return url.toString();
	}
	
}
