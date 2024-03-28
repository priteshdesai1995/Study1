package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.DELETE_ACTIVITIES;
import static com.base.api.constants.PermissionConstants.GET_ACTIVITIES;

import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.dto.filter.ActivityFilter;
import com.base.api.entities.Activity;
import com.base.api.service.ActivityService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/activity")
public class ActivityController {
	@Autowired
	ActivityService activityService;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Gets the activity.
	 *
	 * @param activityFilter the activity filter
	 * @return the activity
	 * @throws ParseException the parse exception
	 */
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Api for get the list of Activities", notes = PERMISSION + GET_ACTIVITIES, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ACTIVITIES })
	public ResponseEntity<TransactionInfo> getActivity(@RequestBody ActivityFilter activityFilter)
			throws ParseException {

		List<Activity> result = activityService.getActivity(activityFilter);

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);

		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Delete activity.
	 *
	 * @param activity_id the activity id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Api for get the delete of Activities", notes = PERMISSION
			+ DELETE_ACTIVITIES, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_ACTIVITIES })
	public ResponseEntity<TransactionInfo> deleteActivity(@RequestParam("activity_id") UUID activity_id) {

		String result = activityService.deleteActivity(activity_id);

		if (result.equals(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("activity_deleted"),
					HttpStatus.OK);
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("activity_delete_fail"));
	}
}
