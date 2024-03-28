package com.base.api.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.base.api.entities.User;
import com.base.api.entities.UserRole;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 3433266357067738937L;
	private Collection<? extends GrantedAuthority> authorities;
	private String password;
	private String username;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private UUID userId;
	private String firstName;
	private String lastName;
	private String email;

	public UserPrincipal(User user, boolean accountNonLocked, boolean accountNonExpired, boolean credentialsNonExpired,
			boolean enabled, UUID userId, Collection<? extends GrantedAuthority> authorities) {
		this.username = user.getUserName();
		this.password = user.getPassword();
		this.authorities = authorities;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.userId = userId;
		this.firstName = user.getUserProfile().getFirstName();
		this.lastName = user.getUserProfile().getLastName();
		this.email = user.getUserProfile().getEmail();
	}

	public static String getRoleAuthority(UserRole role) {
		String name = role.getRoleName().toUpperCase();
		if (!name.startsWith("ROLE_")) {
			name = "ROLE_" + name;
		}
		return name;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getEmail() {
		return email;
	}

	public String getLastName() {
		return lastName;
	}

}
