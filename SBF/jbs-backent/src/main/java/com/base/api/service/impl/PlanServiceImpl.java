package com.base.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.SubscriptionFilter;
import com.base.api.entities.UserPlans;
import com.base.api.exception.APIException;
import com.base.api.exception.PlanNotFoundException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.UserPlanRepo;
import com.base.api.request.dto.PlanDTO;
import com.base.api.service.PlanService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlanServiceImpl implements PlanService {

	@Autowired
	UserPlanRepo userPlanRepo;

	@Autowired
	EntityManager entityManager;

	/**
	 * This method is to get all the plans
	 */
	@Override
	public List<UserPlans> getAllPlanData(SubscriptionFilter filter) throws Exception {

		log.info("PlanServiceImpl : Start getAllPlanData");

		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getName() != null && !filter.getName().isEmpty()) {
			query.append(" LOWER(u.name) LIKE '%" + filter.getName().toLowerCase() + "%' ");
		}
		if (filter.getValidity() != null && !filter.getValidity().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.validity) LIKE '%" + filter.getValidity().toLowerCase() + "%'");
		}
		if (filter.getPrice() != null && !filter.getPrice().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.price) LIKE '%" + filter.getPrice().toLowerCase() + "%'");
		}
		if (filter.getIs_trial_plan() != null && !filter.getIs_trial_plan().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");

			String planType = "Paid";
			if (filter.getIs_trial_plan().equals("1"))
				planType = "Trial";

			query.append(" u.planType = '" + planType.toUpperCase() + "'");
		}

		if (filter.getStart_date() != null && !filter.getStart_date().isEmpty() && filter.getEnd_date() != null
				&& !filter.getEnd_date().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date fromDate = sdf.parse(filter.getStart_date());
			Date toDate = sdf.parse(filter.getEnd_date());

			query.append(" TO_DATE(u.startDate, 'YYYY-MM-DD') >= '" + fromDate
					+ "' and TO_DATE(u.endDate, 'YYYY-MM-DD') <= '" + toDate + "'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSort_type());
		filterBase.setOrder(filter.getOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<UserPlans> results = new ArrayList<UserPlans>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from UserPlans u where");
		return query;
	}

	public List<PlanDTO> getAllPlanData() {

		log.info("PlanServiceImpl : Start getAllPlanData");
		List<UserPlans> planList = userPlanRepo.findAll();
		if (planList == null || planList.isEmpty()) {
			log.error("PlanServiceImpl : getAllPlans data not found");
			throw new APIException("plan.data.not.found", HttpStatus.NOT_FOUND);
		}
		log.info("PlanServiceImpl: End getAllPlans successful");
		return planList.stream().map(e -> new PlanDTO(e)).collect(Collectors.toList());
	}

	/**
	 * This is an implementation to get the plans
	 */
	@Override
	public UserPlans getPlanById(UUID planId) throws Exception {

		log.info("PlanServiceImpl : Get-plan");

		UserPlans userPlan = userPlanRepo.getById(planId);

		if (null != userPlan) {
			log.info("getting user plans ");
			return userPlan;
		} else {
			log.error("No such plan found with plan : " + planId);
			throw new PlanNotFoundException("plan.not.found");
		}
	}

	/**
	 * This method is used to create a user plan
	 */
	@Transactional
	@Override
	public String createPlan(PlanDTO planDTO) {

		log.info("PlanServiceImpl : create-plan");
		UserPlans userPlan = new UserPlans(planDTO);
		try {
			userPlan = userPlanRepo.save(userPlan);
		} catch (DataIntegrityViolationException e) {
			return HttpStatus.FORBIDDEN.name();
		}

		if (userPlan.getId() == null) {
			log.error("some thing went wrong while creating plan");
			throw new APIException("failed.to.create.plan");
		}
		log.info("created plan successfully...!");
		return HttpStatus.OK.name();
	}

	/**
	 * This method is used to update the plan
	 */
	@Transactional
	@Override
	public void updatePlanById(UUID planId, PlanDTO planDTO) throws Exception {

		log.info("PlanServiceImpl : Update-Plan");
		Optional<UserPlans> planOptional = userPlanRepo.findById(planId);
		if (planOptional.isPresent() && null != planOptional.get()) {
			UserPlans userPlan = planOptional.get();
			userPlanRepo.save(userPlan);
			log.info("plan information edited successfully");
		} else
			throw new PlanNotFoundException("invalid.id.was.provided");
	}

	/**
	 * This method will delete the user plan by id.
	 */
	@Override
	public void deletePlanById(UUID planId) throws Exception {

		log.info("PlanServiceImpl : Delete-plan");
		Optional<UserPlans> userPlan = userPlanRepo.findById(planId);
		if (null != userPlan) {
			userPlanRepo.deleteById(planId);
			log.info("plan deleted successfully");
		} else {
			log.error("No such plan found with plan : " + planId);
			throw new PlanNotFoundException("plan.not.found");
		}
	}

}
