package com.base.api.service;

import java.util.List;
import java.util.UUID;

import com.base.api.dto.filter.SubscriptionFilter;
import com.base.api.entities.UserPlans;
import com.base.api.request.dto.PlanDTO;

public interface PlanService {

	
	public UserPlans getPlanById(UUID planId) throws Exception;

	public void deletePlanById(UUID planId) throws Exception;

	public String createPlan(PlanDTO planDTO);

	public void updatePlanById(UUID planId, PlanDTO planDTO) throws Exception;
	
	public List<UserPlans> getAllPlanData(SubscriptionFilter subscriptionFilter) throws Exception;
	
	public List<PlanDTO> getAllPlanData();

	
}
