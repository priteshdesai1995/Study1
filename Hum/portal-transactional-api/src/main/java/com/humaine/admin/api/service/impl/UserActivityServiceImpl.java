package com.humaine.admin.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.admin.api.log.ActivityLogObject;
import com.humaine.admin.api.service.UserActivityService;
import com.humaine.portal.api.model.UserActivity;
import com.humaine.portal.api.model.UserAdmin;
import com.humaine.portal.api.rest.repository.UserAdminActivityRepository;
import com.humaine.portal.api.rest.repository.UserAdminRepository;

@Service
public class UserActivityServiceImpl implements UserActivityService {

	@Autowired
	private UserAdminRepository userRepository;

	@Autowired
	private UserAdminActivityRepository userActivityRepository;

	@Override
	public void saveUserActivity(ActivityLogObject log) {
		UserAdmin user = userRepository.findByUserName(log.getUsername());
		if (user != null) {
			UserActivity _activity = new UserActivity(log, user);
			userActivityRepository.save(_activity);
		}
	}

}
