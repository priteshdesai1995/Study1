package com.humaine.portal.api.security.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jwt.JWTClaimsSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CognitoJwtAuthentication extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 4780644483172376731L;
	private final transient Object principal;
	private JWTClaimsSet jwtClaimsSet;

	public CognitoJwtAuthentication(Object principal, JWTClaimsSet jwtClaimsSet,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.jwtClaimsSet = jwtClaimsSet;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}
}
