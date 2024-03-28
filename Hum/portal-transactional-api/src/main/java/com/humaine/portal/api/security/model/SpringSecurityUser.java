package com.humaine.portal.api.security.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpringSecurityUser implements UserDetails {
	private static final long serialVersionUID = 4089895915973854812L;
	private String username;
	private String password;
	private String email;
	private String accessToken;
	private Integer expiresIn;
	private String tokenType;
	private String refreshToken;
	private String idToken;
	private Date lastPasswordReset;
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean accountNonExpired = true;
	private Boolean accountNonLocked = true;
	private Boolean credentialsNonExpired = true;
	private Boolean enabled = true;

	public SpringSecurityUser(String username, String password, String email, Date lastPasswordReset,
			Collection<? extends GrantedAuthority> authorities) {

		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setLastPasswordReset(lastPasswordReset);
		this.setAuthorities(authorities);
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.getAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.getAccountNonLocked();
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
