package com.humaine.collection.api.security.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.humaine.collection.api.model.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authentication extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 4780644483172376731L;
	private final transient Object principal;
	private Account account;

	public Authentication(Object principal, Collection<? extends GrantedAuthority> authorities, Account account) {
		super(authorities);
		this.principal = principal;
		this.account = account;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}
}
