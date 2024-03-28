package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_STATE;
import static com.base.api.constants.PermissionConstants.DELETE_STATE;
import static com.base.api.constants.PermissionConstants.GET_ALL_STATE;
import static com.base.api.constants.PermissionConstants.GET_STATE_BY_ID;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.SEARCH_COUNTRY;
import static com.base.api.constants.PermissionConstants.UPDATE_STATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.enums.ResponseStatus;
import com.base.api.filter.StateFilter;
import com.base.api.request.dto.StateDTO;
import com.base.api.response.dto.StateTranslableDto;
import com.base.api.service.StateService;
import com.base.api.utils.MetaResponse;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/state")
public class StateController {
	@Autowired
	StateService stateService;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Creates the state.
	 *
	 * @param stateDTO the state DTO
	 * @return the response entity
	 */
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Create state", notes = PERMISSION + CREATE_STATE, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_STATE })
	public ResponseEntity<TransactionInfo> createState(@Valid @RequestBody StateDTO stateDTO) {

		String result = stateService.createState(stateDTO);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("state_created"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("state_create_fail"));
	}

	/**
	 * Gets the state by id.
	 *
	 * @param uuid the uuid
	 * @return the state by id
	 */
	@GetMapping(value = "/get/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get state by Id", notes = PERMISSION + GET_STATE_BY_ID, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_STATE_BY_ID })
	public ResponseEntity<TransactionInfo> getStateById(@PathVariable("uuid") UUID uuid) {

		StateDTO parent = stateService.getStateById(uuid);

		if (parent != null) {

			return ResponseBuilder.buildOkResponse(parent);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(parent, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Search country.
	 *
	 * @param stateFilter the state filter
	 * @return the response entity
	 */
	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Search State", notes = PERMISSION + SEARCH_COUNTRY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { SEARCH_COUNTRY })
	public ResponseEntity<TransactionInfo> searchCountry(@RequestBody StateFilter stateFilter) {
		List<Map<String, Object>> countries = new ArrayList<Map<String, Object>>();
		countries = stateService.searchState(stateFilter);
		int count = stateService.getTotalCount(stateFilter);
		TransactionInfo transactionInfo = new TransactionInfo();
		MetaResponse metaResponse = new MetaResponse();
		metaResponse.setMessage("States Listed successfully");
		metaResponse.setMessageCode(ResponseStatus.SUCCESS.value());
		metaResponse.setStatus(true);
		metaResponse.setStatusCode(HttpStatus.OK.value());
		metaResponse.setCount(count);
		transactionInfo.setResponseData(countries);
		transactionInfo.setMeta(metaResponse);
		return new ResponseEntity<>(transactionInfo, HttpStatus.OK);
	}

	/**
	 * Update state.
	 *
	 * @param stateDTO the state DTO
	 * @param uuid the uuid
	 * @return the response entity
	 */
	@PutMapping(value = "/update/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Update State", notes = PERMISSION + UPDATE_STATE, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_STATE })
	public ResponseEntity<TransactionInfo> updateState(@RequestBody StateDTO stateDTO,
			@PathVariable("uuid") String uuid) {

		String result = stateService.updateState(stateDTO, uuid);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("state_updated"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("state_update_fail"));
	}

	/**
	 * Change status.
	 *
	 * @param statusReq the status req
	 * @return the response entity
	 */
	@PutMapping(value = "/changeStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Change Ctatus of State", notes = PERMISSION
			+ CHANGE_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {

		UUID uuid = UUID.fromString(statusReq.get("uuid"));
		String status = statusReq.get("status");

		String result = stateService.changeStatus(uuid, status);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Gets the all states.
	 *
	 * @param countryUuid the country uuid
	 * @return the all states
	 */
	@GetMapping(value = "/getAllStates/{countryUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get all States by CountryId", notes = PERMISSION
			+ GET_ALL_STATE, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_STATE })
	public ResponseEntity<TransactionInfo> getAllStates(@PathVariable("countryUuid") UUID countryUuid) {
		
		List<StateTranslableDto> states = stateService.getAllStates(countryUuid);

		if (states != null) {

			return ResponseBuilder.buildOkResponse(states);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(states, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Delete state.
	 *
	 * @param stateId the state id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/delete/{stateId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Delete State", notes = PERMISSION + DELETE_STATE, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_STATE })
	public ResponseEntity<TransactionInfo> deleteState(@PathVariable("stateId") UUID stateId) {

		String result = stateService.deleteState(stateId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("state_deleted"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("state_delete_fail"));

	}
}
