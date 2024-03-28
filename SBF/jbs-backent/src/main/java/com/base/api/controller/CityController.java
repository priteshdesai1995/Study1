package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_CITY_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_CITY;
import static com.base.api.constants.PermissionConstants.DELETE_CITY;
import static com.base.api.constants.PermissionConstants.GET_CITY_BY_ID;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.SEARCH_COUNTRY;
import static com.base.api.constants.PermissionConstants.UPDATE_CITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.base.api.filter.CityFilter;
import com.base.api.request.dto.CityDTO;
import com.base.api.service.CityService;
import com.base.api.service.CountryService;
import com.base.api.utils.MetaResponse;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/city")
public class CityController {
	
	@Autowired
	CountryService countryService;

	@Autowired
	CityService cityService;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Creates the city.
	 *
	 * @param cityDto the city dto
	 * @return the response entity
	 */
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Create City", notes = PERMISSION + CREATE_CITY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_CITY })
	public ResponseEntity<TransactionInfo> createCity(@Valid @RequestBody CityDTO cityDto) {

		String result = cityService.createCity(cityDto);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("city_created"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("city_create_fail"));
	}

	/**
	 * Search country.
	 *
	 * @param stateFilter the state filter
	 * @return the response entity
	 */
	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Search City", notes = PERMISSION + SEARCH_COUNTRY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { SEARCH_COUNTRY })
	public ResponseEntity<TransactionInfo> searchCountry(@RequestBody CityFilter stateFilter) {
		List<Map<String, Object>> countries = new ArrayList<Map<String, Object>>();
		countries = cityService.searchCity(stateFilter);
		int count = cityService.getTotalCount(stateFilter);
		TransactionInfo transactionInfo = new TransactionInfo();
		MetaResponse metaResponse = new MetaResponse();
		metaResponse.setMessageCode(ResponseStatus.SUCCESS.value());
		metaResponse.setStatus(true);
		metaResponse.setStatusCode(HttpStatus.OK.value());
		metaResponse.setCount(count);
		transactionInfo.setResponseData(countries);
		transactionInfo.setMeta(metaResponse);
		return new ResponseEntity<>(transactionInfo, HttpStatus.OK);
	}

	/**
	 * Update city.
	 *
	 * @param cityDto the city dto
	 * @param cityId the city id
	 * @return the response entity
	 */
	@PostMapping(value = "/update/{cityId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Update City", notes = PERMISSION + UPDATE_CITY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_CITY })
	public ResponseEntity<TransactionInfo> updateCity(@Valid @RequestBody CityDTO cityDto,
			@PathVariable("cityId") String cityId) {

		String result = cityService.updateCity(cityDto, cityId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("city_updated"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("city_update_fail"));

	}

	/**
	 * Gets the all states.
	 *
	 * @param cityUuid the city uuid
	 * @return the all states
	 */
	@GetMapping(value = "/get/{cityUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to get city by Id", notes = PERMISSION + GET_CITY_BY_ID, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CITY_BY_ID })
	public ResponseEntity<TransactionInfo> getAllStates(@PathVariable("cityUuid") String cityUuid) {

		CityDTO cityDTO = cityService.getCityByUuid(cityUuid);

		if (cityDTO != null) {

			return ResponseBuilder.buildOkResponse(cityDTO);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(cityDTO, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Change status.
	 *
	 * @param statusReq the status req
	 * @return the response entity
	 */
	@PutMapping(value = "/changeStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to change status of city", notes = PERMISSION
			+ CHANGE_CITY_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_CITY_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {
		String uuid = statusReq.get("uuid");
		String status = statusReq.get("status");
		String result = cityService.changeStatus(uuid, status);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Delete city.
	 *
	 * @param cityId the city id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/delete/{cityId}", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to delete city", notes = PERMISSION + DELETE_CITY, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_CITY })
	public ResponseEntity<TransactionInfo> deleteCity(@PathVariable("cityId") String cityId) {

		String result = cityService.deleteCity(cityId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("city_deleted"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("city_delete_fail"));
	}
}
