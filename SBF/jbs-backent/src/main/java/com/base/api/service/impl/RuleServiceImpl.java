package com.base.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.api.dto.filter.FilterBase;
import com.base.api.dto.filter.RuleFilter;
import com.base.api.entities.RuleEntity;
import com.base.api.entities.RuleFilterEntity;
import com.base.api.exception.APIException;
import com.base.api.gateway.util.Util;
import com.base.api.repository.RuleFilterRepository;
import com.base.api.repository.RuleRepository;
import com.base.api.request.dto.RuleFilterDTO;
import com.base.api.service.RuleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RuleServiceImpl implements RuleService {

	@Autowired
	RuleRepository ruleRepository;

	@Autowired
	RuleFilterRepository ruleFilterRepository;

	@Autowired
	EntityManager entityManager;

	@Transactional
	@Override
	public String createRule(RuleFilter ruleFilter) {
		
		log.info("RuleServiceImpl : createRule()");
		RuleEntity ruleEntity = new RuleEntity();
		ruleEntity.setName(ruleFilter.getName());
		ruleEntity.setDescription(ruleFilter.getDescription());
		ruleEntity.setPriority(ruleFilter.getPriority());
		ruleEntity.setOnAction(ruleFilter.getOn_action());
		ruleEntity.setActive("1");

		ruleEntity = ruleRepository.save(ruleEntity);

		for (Map<String, String> filter : ruleFilter.getFilters()) {

			RuleFilterEntity ruleFilterEntity = new RuleFilterEntity();

			ruleFilterEntity.setCreatedDate(LocalDateTime.now());
			ruleFilterEntity.setFilterNo(filter.get("filterNo"));
			ruleFilterEntity.setItemName(filter.get("itemName"));
			ruleFilterEntity.setVerbDescription(filter.get("verbDescription"));
			ruleFilterEntity.setSpecifiedInput(filter.get("specifiedInput"));
			ruleFilterEntity.setTimeFrame(filter.get("timeFrame"));
			ruleFilterEntity.setAction(filter.get("action"));
			ruleFilterEntity.setNotification(filter.get("notification"));
			ruleFilterEntity.setRule(ruleEntity);

			ruleFilterRepository.save(ruleFilterEntity);
		}
		return HttpStatus.OK.name();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleEntity> getRules(RuleFilter filter) {
		
		log.info("RuleServiceImpl : getRules()");
		StringBuilder query = new StringBuilder();
		query = createCommonQuery();

		if (filter.getActive() != null && !filter.getActive().isEmpty()) {
			query.append(" u.active = '" + filter.getActive() + "' ");
		}
		if (filter.getName() != null && !filter.getName().isEmpty()) {
			if (query.indexOf("where") != (query.length() - 5))
				query.append(" and ");
			query.append(" LOWER(u.name) LIKE '%" + filter.getName().toLowerCase() + "%'");
		}

		if (query.indexOf("where") == (query.length() - 5))
			query = new StringBuilder(query.substring(0, query.indexOf("where")));

		FilterBase filterBase = new FilterBase();
		filterBase.setColumnName(filter.getSort_param());
		filterBase.setStartRec(filter.getStartRec());
		filterBase.setEndRec(filter.getEndRec());
		filterBase.setSortingOrder(filter.getSortingOrder());
		String queryParam = Util.getFilterQuery(filterBase, query.toString());
		List<RuleEntity> results = new ArrayList<RuleEntity>();
		results = entityManager.createQuery(queryParam).setFirstResult(Integer.valueOf(filter.getStartRec()))
				.setMaxResults(Integer.valueOf(filter.getEndRec())).getResultList();
		return results;
	}

	private StringBuilder createCommonQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select u from RuleEntity u where");
		return query;
	}

	@Override
	public RuleFilterDTO getRule(UUID ruleId) {
		
		log.info("RuleServiceImpl : getRule");
		RuleFilterDTO ruleFilterDTO = new RuleFilterDTO();

		RuleEntity ruleEntity = ruleRepository.findById(ruleId).get();
		List<RuleFilterEntity> ruleFilterEntities = ruleFilterRepository.findByRule(ruleEntity);

		ruleFilterDTO.setRuleId(ruleEntity.getId());
		ruleFilterDTO.setName(ruleEntity.getName());
		ruleFilterDTO.setDescription(ruleEntity.getDescription());
		ruleFilterDTO.setPriority(ruleEntity.getPriority());
		ruleFilterDTO.setOn_action(ruleEntity.getOnAction());
		ruleFilterDTO.setFilters(ruleFilterEntities);

		return ruleFilterDTO;
	}

	@Override
	public RuleEntity updateRule(RuleFilterDTO ruleFilter) {
		
		log.info("RuleServiceImpl : updateRule");
		RuleEntity ruleEntity = ruleRepository.findById(ruleFilter.getRuleId()).get();

		if (ruleEntity == null) {
			throw new APIException("Rule.data.not.found",HttpStatus.NOT_FOUND);
		}
		ruleEntity.setName(ruleFilter.getName());
		ruleEntity.setDescription(ruleFilter.getDescription());
		ruleEntity.setPriority(ruleFilter.getPriority());
		ruleEntity.setOnAction(ruleFilter.getOn_action());
		ruleEntity = ruleRepository.save(ruleEntity);

		deleteRuleFilters(ruleEntity);

		for (RuleFilterEntity ruleFilterEntity : ruleFilter.getFilters()) {

			ruleFilterEntity.setCreatedDate(LocalDateTime.now());
			ruleFilterEntity.setRule(ruleEntity);
			
			ruleFilterRepository.save(ruleFilterEntity);
		}
		return ruleEntity;
	}

	private void deleteRuleFilters(RuleEntity ruleEntity) {
		for (RuleFilterEntity ruleFilterEntity : ruleFilterRepository.findByRule(ruleEntity)) {
			ruleFilterRepository.delete(ruleFilterEntity);
		}
	}

	@Override
	public String deleteRule(Map<String,UUID> deleteReq) {
		
		log.info("RuleServiceImpl : deleteRule()");
		UUID ruleId = deleteReq.get("ruleId");
		RuleEntity ruleEntity = ruleRepository.findById(ruleId).get();

		if (ruleEntity != null) {
			deleteRuleFilters(ruleEntity);
			ruleRepository.delete(ruleEntity);
			return HttpStatus.OK.name();
		}
		return HttpStatus.NOT_FOUND.name();
	}
}
