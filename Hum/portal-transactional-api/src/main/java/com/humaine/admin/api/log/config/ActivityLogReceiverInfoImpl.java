package com.humaine.admin.api.log.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.humaine.admin.api.log.ActivityLogObject;
import com.humaine.admin.api.log.ActivityLogReceiver;
import com.humaine.admin.api.service.UserActivityService;

@Component
public class ActivityLogReceiverInfoImpl implements ActivityLogReceiver {

	@Autowired
	private UserActivityService userActivityService;

	@Override
	public void receive(ActivityLogObject activityLogObject) {

		userActivityService.saveUserActivity(activityLogObject);
	}
}
