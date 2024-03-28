package com.base.api.service;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import com.base.api.dto.filter.ActivityFilter;
import com.base.api.entities.Activity;

public interface ActivityService {
	List<Activity> getActivity(ActivityFilter activityFilter) throws ParseException;

	String deleteActivity(UUID activity_id);
}
