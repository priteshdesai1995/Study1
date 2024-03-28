package com.humaine.admin.api.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.humaine.admin.api.model.User;
import com.humaine.admin.api.service.UserService;

@Component
public class UserConfigMetaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6492158228205882312L;

	@Autowired
	private UserService userService;

	public Map<String, Object> getUserRelatedInformation(String userName) {
		Map<String, Object> maps = new HashMap<>();
		User userEntity = userService.findByUserName(userName);
		maps.put("user_role", userEntity.getUserRole());
		maps.put("userName", userEntity.getUserName());
		maps.put("fullName", userEntity.getUserProfile().getFirstName().concat(" ")
				.concat(userEntity.getUserProfile().getLastName()));
		maps.put("email", userEntity.getUserProfile().getEmail());
		maps.put("userId", userEntity.getUserId());
		return maps;
	}

}
