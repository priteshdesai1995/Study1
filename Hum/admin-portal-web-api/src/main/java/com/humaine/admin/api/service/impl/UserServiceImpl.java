package com.humaine.admin.api.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.humaine.admin.api.enums.UserStatus;
import com.humaine.admin.api.exception.APIException;
import com.humaine.admin.api.model.Privilege;
import com.humaine.admin.api.model.User;
import com.humaine.admin.api.model.UserPrincipal;
import com.humaine.admin.api.model.UserRole;
import com.humaine.admin.api.repository.UserRepository;
import com.humaine.admin.api.service.UserService;
import com.humaine.admin.api.util.ErrorMessageUtils;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

	private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ErrorMessageUtils errorMessageUtils;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		log.info("loadUserByUsername() method call...");
		User user = userRepository.findByUserNameAndStatus(userName, UserStatus.ACTIVE.getStatus());
		if (user == null) {
			log.error("User not found or user deactivated.");
			throw new APIException(this.errorMessageUtils.getMessageWithCode("api.error.user.not.found",
					new Object[] {}, "api.error.user.not.found.code"));
		}
		UserPrincipal principle = new UserPrincipal(user, user.getAccountNonLocked(), user.getAccountNonExpired(),
				user.getCredentialsNonExpired(), user.getEnabled(), user.getUserId(),
				getAuthorities(user.getUserRole()));
		return principle;
	}

	@Override
	public User findByUserName(String userName) {
		log.info("signup() method call...");
		return userRepository.findByUserName(userName);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(UserRole role) {
		return getGrantedAuthorities(getPrivileges(role));
	}

	private List<String> getPrivileges(UserRole role) {
		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		privileges.add(UserPrincipal.getRoleAuthority(role));
		collection.addAll(role.getPrivileges());
		for (Privilege item : collection) {
			privileges.add(item.getAuthority());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}
