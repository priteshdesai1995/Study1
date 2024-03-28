package com.humaine.portal.api.security.filter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.humaine.portal.api.security.config.AWSConfig;
import com.humaine.portal.api.security.config.CognitoJwtAuthentication;
import com.humaine.portal.api.security.exception.CognitoException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

public class AwsCognitoIdTokenProcessor {

	private static final String INVALID_TOKEN = "Invalid Access Token";
	private static final String NO_TOKEN_FOUND = "Invalid Action, no token found";

	private static final String EMPTY_STRING = "";

	@SuppressWarnings("rawtypes")
	@Autowired
	private ConfigurableJWTProcessor configurableJWTProcessor;

	@Autowired
	private AWSConfig jwtConfiguration;

	/**
	 * Method that verifies if the token has the Bearer string key and if it does it
	 * removes it.
	 */
	public static String extractAndDecodeJwt(String token) throws Exception {
		if (token != null && token.startsWith("Bearer ") == false) {
			throw new CognitoException(INVALID_TOKEN, CognitoException.NOT_A_TOKEN_EXCEPTION, INVALID_TOKEN);
		}
		return token.substring("Bearer ".length());
	}

	@SuppressWarnings("unchecked")
	public Authentication getAuthentication(HttpServletRequest request)
			throws ParseException, BadJOSEException, JOSEException, CognitoException, Exception {
		String encryptedToken = request.getHeader(jwtConfiguration.getHttpHeader());

		if (encryptedToken == null) {
			throw new CognitoException(NO_TOKEN_FOUND, CognitoException.NO_TOKEN_PROVIDED_EXCEPTION,
					"No token found in Http Authorization Header");
		} else {
			final String idToken = extractAndDecodeJwt(encryptedToken);
			JWTClaimsSet claimsSet = null;
			/**
			 * To verify JWT claims
			 */
			claimsSet = configurableJWTProcessor.process(idToken, null);

			if (!isIssuedCorrectly(claimsSet)) {
				throw new CognitoException(INVALID_TOKEN, CognitoException.INVALID_TOKEN_EXCEPTION_CODE,
						String.format("Issuer %s in JWT token doesn't match cognito idp %s", claimsSet.getIssuer(),
								jwtConfiguration.getCognitoIdentityPoolUrl()));
			}
			if (!isIdToken(claimsSet)) {
				throw new CognitoException(INVALID_TOKEN, CognitoException.NOT_A_TOKEN_EXCEPTION,
						"JWT Token doesn't seem to be an ID Token");
			}
			String username = claimsSet.getClaims().get(jwtConfiguration.getUserNameField()).toString();
			String email = claimsSet.getClaims().get(jwtConfiguration.getEmailField()).toString();
			Boolean isVerified = (Boolean) claimsSet.getClaims().get(jwtConfiguration.getEmailVerifiedField());

			if (!isVerified) {
				throw new CognitoException(INVALID_TOKEN, CognitoException.NOT_A_TOKEN_EXCEPTION,
						"JWT Token doesn't seem to be an ID Token");
			}
			List<String> groups = (List<String>) claimsSet.getClaims().get(jwtConfiguration.getGroupsField());
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			User user = new User(email, EMPTY_STRING, grantedAuthorities);
			return new CognitoJwtAuthentication(user, claimsSet, grantedAuthorities);
		}

	}

	/**
	 * Method that validates if the tokenId is issued correctly.
	 */
	private boolean isIssuedCorrectly(JWTClaimsSet claimsSet) {
		return claimsSet.getIssuer().equals(jwtConfiguration.getCognitoIdentityPoolUrl());
	}

	/**
	 * Method that validates if the ID token is valid.
	 */
	private boolean isIdToken(JWTClaimsSet claimsSet) {
		return claimsSet.getClaim("token_use").equals("id");
	}

	/**
	 * Method generics
	 */
	private static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}
}
