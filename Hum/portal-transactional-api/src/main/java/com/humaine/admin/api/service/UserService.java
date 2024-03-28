package com.humaine.admin.api.service;

import com.humaine.portal.api.model.UserAdmin;

public interface UserService {

	public UserAdmin findByUserName(String userName);

}
