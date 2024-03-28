package com.base.api.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.base.api.dto.filter.RuleFilter;
import com.base.api.entities.RuleEntity;
import com.base.api.request.dto.RuleFilterDTO;


public interface RuleService {

	String createRule(RuleFilter ruleFilter);

	List<RuleEntity> getRules(RuleFilter ruleFilter);

	RuleFilterDTO getRule(UUID ruleId);

	RuleEntity updateRule(RuleFilterDTO ruleFilter);

	String deleteRule(Map<String, UUID> deleteReq);

}
