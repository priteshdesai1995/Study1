package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_PLAN_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_PLAN;
import static com.base.api.constants.PermissionConstants.DELETE_PLAN;
import static com.base.api.constants.PermissionConstants.GET_ALL_PLANS;
import static com.base.api.constants.PermissionConstants.GET_PLAN;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_PLAN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.dto.filter.SubscriptionFilter;
import com.base.api.entities.UserPlans;
import com.base.api.repository.UserPlanRepo;
import com.base.api.request.dto.PlanDTO;
import com.base.api.service.PlanService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 *         This class is created for the rest controllers of the plan API
 */

@RequestMapping("/subscription")
@Api(tags = "Manage subscription API", description = "RESTApi for the Manage subscription")
@Slf4j
@RestController
public class PlanController {

	@Autowired
	PlanService planService;

	@Autowired
	UserPlanRepo planRepo;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * @return This rest API is to get all the plans
	 */
	@PostMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to Get all the plan", notes = PERMISSION + GET_ALL_PLANS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_PLANS })
	public ResponseEntity<TransactionInfo> getAllPlanData(@RequestBody SubscriptionFilter subscriptionFilter)
			throws Exception {

		log.info("PlanController: getAllPlanData");
		List<UserPlans> result = planService.getAllPlanData(subscriptionFilter);
		log.info("PlanController: getAllPlanData successful");
		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * @param planId
	 * @return
	 * @throws Exception This rest API is to get the plan
	 */
	@GetMapping(value = "/show")
	@ApiOperation(value = "API Endpoint to Get the plan", notes = PERMISSION + GET_PLAN, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_PLAN })
	public ResponseEntity<TransactionInfo> getPlan(@Valid @RequestParam("subscription_id") UUID planId)
			throws Exception {

		log.info("API hit of the get plan");
		Optional<UserPlans> userPlan = planRepo.findById(planId);
		if (!userPlan.isPresent()) {
			return ResponseBuilder.buildRecordNotFoundResponse(userPlan, resourceBundle.getString("record_not_found"));
		}
		UserPlans planData = planService.getPlanById(planId);
		log.info("Get the plan successfully");
		return ResponseBuilder.buildOkResponse(new PlanDTO(planData));

	}

	/**
	 * @param planDTO
	 * @return
	 * @throws Exception
	 * 
	 *                   This rest API is to create plan
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "API Endpoint to create the plan", notes = PERMISSION + CREATE_PLAN, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_PLAN })
	public ResponseEntity<TransactionInfo> createPlan(@Valid @RequestBody PlanDTO planDTO) throws Exception {
		log.info("API hit for the create plan");
		String result = planService.createPlan(planDTO);
		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("subscription_created"),
					HttpStatus.CREATED);

		} else if (result.equals(HttpStatus.FORBIDDEN.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("subscription_exist"),
					HttpStatus.FORBIDDEN);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("subscription_create_fail"));
	}

	/**
	 * @param planDTO
	 * @param planId
	 * @return
	 * @throws Exception This rest API is to update plan
	 */
	@SuppressWarnings("unused")
	@PostMapping(value = "/update")
	@ApiOperation(value = "API Endpoint to Update the plan", notes = PERMISSION + UPDATE_PLAN, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_PLAN })
	public ResponseEntity<TransactionInfo> updatePlan(@Valid @NotNull @RequestBody PlanDTO planDTO,
			@RequestParam("subscription_id") UUID planId) throws Exception {

		log.info("API hit of the update plan");
		Optional<UserPlans> userPlan = planRepo.findById(planId);
		UserPlans plan = userPlan.get();
		if (userPlan == null) {
			log.error("Not able to fetch userplan");
			return ResponseBuilder.buildInternalServerErrorResponse(plan,
					resourceBundle.getString("subscription_update_fail"));
		}
		plan.update(planDTO);
		UserPlans userplan = planRepo.save(plan);
		if (userplan != null) {
			return ResponseBuilder.buildCRUDResponse(plan, resourceBundle.getString("subscription_updated"),
					HttpStatus.OK);
		}
		log.info("plan updated successfully..!");
		return ResponseBuilder.buildCRUDResponse(plan, resourceBundle.getString("subscription_exist"),
				HttpStatus.FORBIDDEN);
	}

	/**
	 * @param planId
	 * @return
	 * @throws Exception This rest API is to delete the plan
	 */
	@DeleteMapping(value = "/delete")
	@ApiOperation(value = "API Endpoint to Delete the plan", notes = PERMISSION + DELETE_PLAN, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_PLAN })
	public ResponseEntity<TransactionInfo> deletePlan(@RequestParam("subscription_id") UUID planId) throws Exception {

		log.info("API hit of the delete plan");
		Optional<UserPlans> userPlan = planRepo.findById(planId);
		if (!userPlan.isPresent()) {
			log.error("no data found");
			return ResponseBuilder.buildRecordNotFoundResponse(userPlan.get(),
					resourceBundle.getString("record_not_found"));
		}
		planService.deletePlanById(planId);
		log.info("Delete the plan successfully");
		return ResponseBuilder.buildCRUDResponse(userPlan, resourceBundle.getString("subscription_deleted"),
				HttpStatus.OK);
	}

	@ApiOperation(value = "update planStatus", notes = " updatePlanStatus", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/change_status", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CHANGE_PLAN_STATUS })
	public ResponseEntity<TransactionInfo> updatePlanStatus(@RequestBody Map<String, String> statusReq) {

		log.info("PlanController:updatePlanStatus");
		UserPlans plan = planRepo.findById(UUID.fromString(statusReq.get("subscription_id"))).get();

		if (plan != null) {
			plan.setStatus(statusReq.get("status"));
			planRepo.save(plan);
			log.info("Plan Status Changed Successfully");
			return ResponseBuilder.buildStatusChangeResponse(plan, resourceBundle.getString("status"));
		}
		return ResponseBuilder.buildRecordNotFoundResponse(plan, resourceBundle.getString("record_not_found"));
	}

}
