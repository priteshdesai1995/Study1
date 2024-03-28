package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_TEMPLATES_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_TEMPLATES;
import static com.base.api.constants.PermissionConstants.GET_ALL_TEMPLATES;
import static com.base.api.constants.PermissionConstants.GET_TEMPLATE_BY_ID;
import static com.base.api.constants.PermissionConstants.UPDATE_TEMPLATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.entities.Template;
import com.base.api.request.dto.TemplateDTO;
import com.base.api.service.TemplateService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/template")
@Api(tags = "Mail Management API", description = "Rest APIs for the mail management")
@ApiOperation(value = "User sign up API Endpoint")

public class TemplateController {

	@Autowired
	TemplateService templateService;

	@ApiOperation(value = "create template", notes = "createTemplate", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/create", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CREATE_TEMPLATES })
	public ResponseEntity<TransactionInfo> createTemplate(@RequestBody TemplateDTO templateDTO) {
		log.info("createTemplate : TemplateController ");
		templateService.create(templateDTO);
		log.info("createTemplate called succcessfully");
		return ResponseBuilder.buildWithMessage("api.success.template.create");
	}

	@ApiOperation(value = "get all templates", notes = "getAllTemplates", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@GetMapping(value = "/list", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_ALL_TEMPLATES })
	public ResponseEntity<TransactionInfo> getAllTemplates() {
		log.info("getAllTemplates : TemplateController ");
		List<TemplateDTO> result = templateService.getAllTemplates();
		log.info("getAllTemplate called successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@ApiOperation(value = "getTemplate by id", notes = "getTemplateById", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@GetMapping(value = "/get/{id}", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_TEMPLATE_BY_ID })
	public ResponseEntity<TransactionInfo> getTemplateById(@PathVariable("id") UUID id) {
		log.info("getTemplateById : TemplateController");
		TemplateDTO result = templateService.getTemplateById(id);
		log.info("getTemplateById called successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@ApiOperation(value = "update template", notes = "updateTemplate", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/update/{id}", headers = {
			"Version=V1" }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { UPDATE_TEMPLATE })
	public ResponseEntity<TransactionInfo> updateTemplate(@RequestBody TemplateDTO templateDTO,
			@PathVariable("id") UUID id) {
		log.info("updateTemplate : TemplateController");
		Template result = templateService.update(templateDTO, id);
		log.info("updateTemplate called successfully");
		return ResponseBuilder.buildOkResponse(result);
	}

	@ApiOperation(value = "change status", notes = "changeStatus", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PutMapping(value = "/changeStatus", headers = { "Version=V1" }, produces = APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CHANGE_TEMPLATES_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {
		log.info("changeStatus : TemplateController");
		UUID id = UUID.fromString(statusReq.get("id"));
		String status = statusReq.get("status");
		templateService.changeStatus(id, status);
		return ResponseBuilder.buildWithMessage("api.success.changestatus");
	}

}
