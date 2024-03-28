package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.ADD_EDIT_CONTENT;
import static com.base.api.constants.PermissionConstants.CHANGE_CONTENT_STATUS;
import static com.base.api.constants.PermissionConstants.CMS_PAGE_LOAD;
import static com.base.api.constants.PermissionConstants.CREATE_CONTENT;
import static com.base.api.constants.PermissionConstants.GET_CONTENT;
import static com.base.api.constants.PermissionConstants.GET_CONTENTS;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_CONTENT;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.base.api.entities.ContentManagement;
import com.base.api.request.dto.ContentManagementDTO;
import com.base.api.request.dto.ContentPageLoadDTO;
import com.base.api.response.dto.ContentListDTO;
import com.base.api.service.ContentService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/content")
@Slf4j
public class ContentManagementController {

	@Autowired
	ContentService contentService;

	@Autowired
	ResourceBundle resourceBundle;
	
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to create content", notes = PERMISSION + CREATE_CONTENT , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_CONTENT })
	public ResponseEntity<TransactionInfo> createContent(@Valid @RequestBody ContentManagementDTO contentManagement){
		String result = contentService.create(contentManagement);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("content_created"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("content_create_fail"));
	}
	
	@PutMapping(value = "/update/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to update content", notes = PERMISSION + UPDATE_CONTENT , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_CONTENT })
	public ResponseEntity<TransactionInfo> updateContentPage(@PathVariable("pageId") String pageId,
			@Valid @RequestBody ContentManagementDTO contentManagement) {
		
		String result = contentService.update(contentManagement, pageId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("content_updated"),
					HttpStatus.OK);
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("content_update_fail"));
		
	}
	
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to get list of content", notes = PERMISSION + GET_CONTENTS , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CONTENTS })
	public ResponseEntity<TransactionInfo> getcontentList() { 

		List<ContentListDTO> contents = contentService.getContents();

		if (contents != null) {

			return ResponseBuilder.buildOkResponse(contents);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(contents, resourceBundle.getString("record_not_found"));

	}
	
	@GetMapping(value = "/get/{pageId}",  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to get single content", notes = PERMISSION + GET_CONTENT , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CONTENT })
	public ResponseEntity<TransactionInfo> getcontentPageById(@PathVariable("pageId") String pageId) {
		ContentManagement content = contentService.getContentById(pageId);

		if (content != null) {

			return ResponseBuilder.buildOkResponse(content);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(content, resourceBundle.getString("record_not_found"));
	}
		
	@PutMapping(value = "/changeStatus",  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to change content status", notes = PERMISSION + CHANGE_CONTENT_STATUS , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_CONTENT_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {
		
		String id = statusReq.get("id");
		String status = statusReq.get("status");
		String result = contentService.changeStatus(id, status);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));

	}
	
	@GetMapping(value = "/cmsPageLoad/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to page load for cms", notes = PERMISSION + CMS_PAGE_LOAD , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CMS_PAGE_LOAD })
	public ResponseEntity<TransactionInfo> cmsPageLoad(@PathVariable("pageId") String pageId) { 
		ContentPageLoadDTO contentDTO = contentService.pageLoad(pageId);
		if (contentDTO != null) {

			return ResponseBuilder.buildOkResponse(contentDTO);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(contentDTO, resourceBundle.getString("record_not_found"));

	}
	
	@PostMapping(value = "/addEditContent/{pageId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to add edit content", notes = PERMISSION + ADD_EDIT_CONTENT , authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ADD_EDIT_CONTENT })
	public ResponseEntity<TransactionInfo> addEditContent(@Valid @RequestBody ContentPageLoadDTO contentManagement,
			@PathVariable("pageId") String pageId) {
		String result = contentService.addContent(contentManagement, pageId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("content_updated"),
					HttpStatus.OK);
		} else if (result.equals(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("content_update_fail"));

	}
	
}
