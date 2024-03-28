package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.GET_CONTACTS;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.filter.ContactFilter;
import com.base.api.response.dto.ContactDTO;
import com.base.api.service.ContactService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/contact")
@Slf4j
public class ContactController {

	@Autowired
	ContactService contactService;
	
	@Autowired
	ResourceBundle resourceBundle;
	
	@PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Api for get all contacts", notes = PERMISSION + GET_CONTACTS, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CONTACTS })
	public ResponseEntity<TransactionInfo> getAllContacts(@RequestBody ContactFilter contactFilter){
		
		List<ContactDTO> result = contactService.getAllContacts(contactFilter);
		
		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}
		
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}
	
	
}
