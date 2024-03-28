package com.humaine.portal.api.security.authentication;

public class CognitoJwtIdTokenCredentialsHolder {
	private String idToken;

	public String getIdToken() {
		return idToken;
	}

	public CognitoJwtIdTokenCredentialsHolder setIdToken(String idToken) {
		this.idToken = idToken;
		return this;
	}
}
