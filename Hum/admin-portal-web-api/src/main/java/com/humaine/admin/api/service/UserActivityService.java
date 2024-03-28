package com.humaine.admin.api.service;

import com.humaine.admin.api.log.ActivityLogObject;

public interface UserActivityService {
	
	void saveUserActivity(ActivityLogObject log);
	
}
