package com.base.api.controller;

import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.CHANGE_REPORT_STATUS;
import static com.base.api.constants.PermissionConstants.DELETE_REPORT;
import static com.base.api.constants.PermissionConstants.GET_REPORTS;
import static com.base.api.constants.RoleConstants.ROLE_SUPER_ADMIN;
import static com.base.api.constants.RoleConstants.ROLE_USER;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.dto.filter.ReviewFilter;
import com.base.api.entities.Report;
import com.base.api.service.ReportService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author preyansh_prajapati
 * 
 *         This controller class have methods that get called when API is get
 *         hit
 */
@RestController
@RequestMapping("/report")
@Slf4j
@Api(tags = "User Report managemente API", description = "Rest API for User Report managemente")
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	ResourceBundle resourceBundle;

	@GetMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to get all the user reports ", notes = PERMISSION
			+ GET_REPORTS, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_REPORTS })
	@Secured({ ROLE_SUPER_ADMIN, ROLE_USER })
	public ResponseEntity<TransactionInfo> getAllReports() {

		log.info("ReportController : getAllReports");

		List<Report> allReports = reportService.getAllReports();

		if (allReports != null) {

			return ResponseBuilder.buildOkResponse(allReports);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(allReports, resourceBundle.getString("record_not_found"));
	}

	@PostMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to get all the user reports ", notes = PERMISSION
			+ GET_REPORTS, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_REPORTS })
	public ResponseEntity<TransactionInfo> getReports(@RequestBody ReviewFilter reviewFilter) {

		log.info("ReportController : getReports");

		List<Report> result = reportService.getReports(reviewFilter);

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	@PostMapping(value = "/change_status")
	@ApiOperation(value = "API Endpoint to change the status of the report ", notes = PERMISSION
			+ CHANGE_REPORT_STATUS, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_REPORT_STATUS })
	@Secured({ ROLE_SUPER_ADMIN, ROLE_USER })
	public ResponseEntity<TransactionInfo> changeReportStatus(@RequestBody Map<String, String> statusReq) {

		log.info("ReportController : changeReportStatus");

		Report report = reportService.changeStatus(statusReq);

		if (report != null) {
			return ResponseBuilder.buildStatusChangeResponse(report, resourceBundle.getString("status"));

		}

		log.info("ReportController : status changed successfully");

		return ResponseBuilder.buildRecordNotFoundResponse(report, resourceBundle.getString("record_not_found"));
	}

	@DeleteMapping(value = "/delete")
	@ApiOperation(value = "API Endpoint to change the status of the report ", notes = PERMISSION
			+ DELETE_REPORT, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_REPORT })
	@Secured({ ROLE_SUPER_ADMIN, ROLE_USER })
	public ResponseEntity<TransactionInfo> deleteReport(@RequestParam("report_id") UUID reportId) {

		log.info("ReportController : deleteReport");

		String result = reportService.deleteReport(reportId);

		log.info("ReportController : report deleted successfully successfully");

		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("report_deleted"), HttpStatus.OK);

		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("report_delete_fail"));
	}

}
