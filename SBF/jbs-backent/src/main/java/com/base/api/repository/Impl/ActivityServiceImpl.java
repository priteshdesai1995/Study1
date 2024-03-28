package com.base.api.repository.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.base.api.dto.filter.ActivityFilter;
import com.base.api.dto.filter.FilterBase;
import com.base.api.entities.Activity;
import com.base.api.gateway.util.Util;
import com.base.api.repository.ActivityRepository;
import com.base.api.service.ActivityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	EntityManager entityManager;

	@Autowired
	ActivityRepository activityRepository;

	/**
	 * Gets the activity.
	 *
	 * @param filter the filter
	 * @return the activity
	 * @throws ParseException the parse exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getActivity(ActivityFilter filter) throws ParseException {
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getName() != null && !filter.getName().isEmpty()) {
			query.append(" LOWER(u.user.userName) LIKE '%" + filter.getName().toLowerCase() + "%' ");
		}
		if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.user.userProfile.email) LIKE '%" + filter.getEmail().toLowerCase() + "%'");
		}
		if (filter.getActivity_desc() != null && !filter.getActivity_desc().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.action) LIKE '%" + filter.getActivity_desc().toLowerCase() + "%'");
		}
		if (filter.getFrom_date() != null && !filter.getFrom_date().isEmpty() && filter.getTo_date() != null
				&& !filter.getTo_date().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Date fromDate = sdf.parse(filter.getFrom_date());
			Date toDate = sdf.parse(filter.getTo_date());

			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			toDate = c.getTime();

			query.append(" u.createdAt >= '" + fromDate + "' and u.createdAt <= '" + toDate + "'");
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
		List<Activity> results = new ArrayList<Activity>();
		
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	/**
	 * Creates the common query.
	 *
	 * @return the string builder
	 */
	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from Activity u where");
		return query;
	}

	/**
	 * Delete activity.
	 *
	 * @param activity_id the activity id
	 * @return the string
	 */
	@Override
	public String deleteActivity(UUID activity_id) {
		Activity activityEntity = activityRepository.findById(activity_id).get();

		if (activityEntity == null)
			return HttpStatus.NOT_FOUND.name();

		activityRepository.delete(activityEntity);

		return HttpStatus.OK.name();
	}

}
