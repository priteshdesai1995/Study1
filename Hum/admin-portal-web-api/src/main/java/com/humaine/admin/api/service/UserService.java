package com.humaine.admin.api.service;

import com.humaine.admin.api.model.User;

public interface UserService {

	public User findByUserName(String userName);

}
