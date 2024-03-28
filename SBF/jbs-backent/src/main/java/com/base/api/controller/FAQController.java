package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_FAQ_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_FAQ;
import static com.base.api.constants.PermissionConstants.DELETE_FAQ;
import static com.base.api.constants.PermissionConstants.GET_ALL_FAQS;
import static com.base.api.constants.PermissionConstants.GET_ALL_FAQS_TOPICS;
import static com.base.api.constants.PermissionConstants.GET_FAQS_BY_ID;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_FAQ;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.base.api.request.dto.FAQDTO;
import com.base.api.response.dto.FAQTopicDTO;
import com.base.api.service.FAQService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
@Slf4j
@RestController
@RequestMapping("/faq")
public class FAQController {

	@Autowired
	FAQService faqService;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Gets the all Faqs.
	 *
	 * @return the all Faqs
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "API Endpoint to get the all FAQs", notes = PERMISSION + GET_ALL_FAQS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_FAQS })
	public ResponseEntity<TransactionInfo> getAllFAQs() {

		List<FAQDTO> parent = faqService.getFAQList();

		log.info("parent = " + parent.toString());
		if (parent != null) {

			return ResponseBuilder.buildOkResponse(parent);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(parent, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Creates the FAQ.
	 *
	 * @param entity the entity
	 * @return the response entity
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "API Endpoint to create the FAQs", notes = PERMISSION + CREATE_FAQ, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_FAQ })
	public ResponseEntity<TransactionInfo> createFAQ(@RequestBody() FAQDTO entity) {

		String result = faqService.createFAQ(entity);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("faq_created"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("faq_create_fail"));
	}

	/**
	 * Gets the all FAQ topics.
	 *
	 * @return the all FAQ topics
	 */
	@GetMapping(value = "/faqTopic/list")
	@ApiOperation(value = "API Endpoint to get the all FAQs topics", notes = PERMISSION
			+ GET_ALL_FAQS_TOPICS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_FAQS_TOPICS })
	public ResponseEntity<TransactionInfo> getAllFAQTopics() {

		List<FAQTopicDTO> parent = faqService.getFAQTopicList();

		if (parent != null) {

			return ResponseBuilder.buildOkResponse(parent);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(parent, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Change status.
	 *
	 * @param statusReq the status req
	 * @return the response entity
	 */
	@PutMapping(value = "/changeStatus")
	@ApiOperation(value = "API Endpoint to change the FAQs Status", notes = PERMISSION
			+ CHANGE_FAQ_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_FAQ_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {

		UUID id = UUID.fromString(statusReq.get("id"));
		String status = statusReq.get("status");

		String result = faqService.changeStatus(id, status);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Gets the FAQ by id.
	 *
	 * @param faqId the faq id
	 * @return the FAQ by id
	 */
	@GetMapping(value = "/get/{faqId}")
	@ApiOperation(value = "API Endpoint to get faq by Id", notes = PERMISSION + GET_FAQS_BY_ID, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_FAQS_BY_ID })
	public ResponseEntity<TransactionInfo> getFAQById(@PathVariable("faqId") UUID faqId) {

		log.info("get by id called");

		FAQDTO parent = faqService.getFAQ(faqId);

		if (parent != null) {

			return ResponseBuilder.buildOkResponse(parent);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(parent, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Update FAQ.
	 *
	 * @param faqdto the faqdto
	 * @param id     the id
	 * @return the response entity
	 */
	@PutMapping(value = "/update/{id}")
	@ApiOperation(value = "API Endpoint to update faq", notes = PERMISSION + UPDATE_FAQ, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_FAQ })
	public ResponseEntity<TransactionInfo> updateFAQ(@RequestBody FAQDTO faqdto, @PathVariable("id") UUID id) {

		String result = faqService.update(faqdto, id);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("faq_updated"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("faq_update_fail"));
	}

	/**
	 * Delete FAQ.
	 *
	 * @param faqId the faq id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/delete/{faqId}")
	@ApiOperation(value = "API Endpoint to delete faq", notes = PERMISSION + DELETE_FAQ, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_FAQ })
	public ResponseEntity<TransactionInfo> deleteFAQ(@PathVariable("faqId") UUID faqId) {

		String result = faqService.deleteFAQ(faqId);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("faq_deleted"), HttpStatus.OK);
		} else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("faq_delete_fail"));
	}

}
