package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CREATE_RULE;
import static com.base.api.constants.PermissionConstants.DELETE_RULE;
import static com.base.api.constants.PermissionConstants.GET_ALL_RULES;
import static com.base.api.constants.PermissionConstants.GET_RULE;
import static com.base.api.constants.PermissionConstants.UPDATE_RULE;
import static com.base.api.constants.PermissionConstants.UPDATE_RULE_STATUS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.dto.filter.RuleFilter;
import com.base.api.entities.RuleEntity;
import com.base.api.exception.APIException;
import com.base.api.repository.RuleRepository;
import com.base.api.request.dto.RuleFilterDTO;
import com.base.api.service.RuleService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rule")
public class RuleController {

	@Autowired
	RuleService ruleService;

	@Autowired
	RuleRepository ruleRepository;

	@ApiOperation(value = "create rule", notes = " createRule", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/create", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CREATE_RULE })
	public ResponseEntity<TransactionInfo> createRule(@RequestBody RuleFilter ruleFilter) throws Exception {

		log.info("RuleController: createRule");
		ruleService.createRule(ruleFilter);
		log.info("RuleController: rule added successfully");
		return ResponseBuilder.buildWithMessageAndStatus("Rule.created.successfully", HttpStatus.CREATED);
	}

	@ApiOperation(value = "get rules", notes = " getRules", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/list", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_ALL_RULES })
	public ResponseEntity<TransactionInfo> getRules(@RequestBody RuleFilter ruleFilter) {

		log.info("RuleController:getRules");
		List<RuleEntity> result = ruleService.getRules(ruleFilter);
		if (result == null) {
			throw new APIException("rules.not.found", HttpStatus.NOT_FOUND);
		}
		log.info("RuleController:getRules Successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@PostMapping(value = "/view", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get rule", notes = " getRule", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_RULE })
	public ResponseEntity<TransactionInfo> getRule(@RequestParam("rule_id") UUID ruleId) {

		log.info("RuleController:getRule");
		RuleFilterDTO result = ruleService.getRule(ruleId);
		if (result == null) {
			throw new APIException("rule.not.found", HttpStatus.NOT_FOUND);
		}
		log.info("RuleController:getRule Successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@ApiOperation(value = "update rule", notes = " updateRule", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/update", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { UPDATE_RULE })
	public ResponseEntity<TransactionInfo> updateRule(@RequestBody RuleFilterDTO ruleFilter) {

		log.info("RuleController:updateRule");
		RuleEntity result = ruleService.updateRule(ruleFilter);
		log.info("Rule updated successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@ApiOperation(value = "delete rule", notes = " deleteRule", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/delete", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { DELETE_RULE })
	public ResponseEntity<TransactionInfo> deleteRule(@RequestBody Map<String, UUID> deleteReq) {

		log.info("RuleController:deleteRule");

		String result = ruleService.deleteRule(deleteReq);

		if (result.equals(HttpStatus.OK.name())) {
			return ResponseBuilder.buildWithMessage("Rule_deleted");
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {
			return ResponseBuilder.buildWithMessage("Record_not_found");
		}
		return ResponseBuilder.buildWithMessage("Rule_delete_fail");
	}

	@ApiOperation(value = "update ruleStatus", notes = " updateRuleStatus", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/change_status", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { UPDATE_RULE_STATUS })
	public ResponseEntity<TransactionInfo> updateRuleStatus(@RequestBody Map<String, String> statusReq) {

		log.info("RuleController:updateRuleStatus");
		RuleEntity ruleEntity = ruleRepository.findById(UUID.fromString(statusReq.get("ruleId"))).get();

		if (ruleEntity != null) {
			ruleEntity.setActive(statusReq.get("status"));
			ruleRepository.save(ruleEntity);
			log.info("Rule Status Changed Successfully");
			return ResponseBuilder.buildWithMessage("status");
		}
		return ResponseBuilder.buildWithMessage("Record_not_found");
	}
}
