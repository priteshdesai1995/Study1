package com.humaine.admin.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.humaine.admin.api.log.ActivityLogObject;
import com.humaine.admin.api.model.User;
import com.humaine.admin.api.model.UserActivity;
import com.humaine.admin.api.repository.UserActivityRepository;
import com.humaine.admin.api.repository.UserRepository;
import com.humaine.admin.api.service.UserActivityService;

@Service
public class UserActivityServiceImpl implements UserActivityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserActivityRepository userActivityRepository;

	@Override
	public void saveUserActivity(ActivityLogObject log) {
		User user = userRepository.findByUserName(log.getUsername());
		if (user != null) {
			UserActivity _activity = new UserActivity(log, user);
			userActivityRepository.save(_activity);
		}
	}

}
