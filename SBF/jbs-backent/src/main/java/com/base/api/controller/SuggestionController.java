package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.config.GeneratePdfReport;
import com.base.api.dto.filter.SuggestionFilter;
import com.base.api.entities.Suggestion;
import com.base.api.repository.SuggestionRepository;
import com.base.api.request.dto.SuggestionDTO;
import com.base.api.request.dto.SuggestionRequest;
import com.base.api.service.SuggestionService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javassist.NotFoundException;


@RequestMapping("/suggestion")
@Api(tags = "suggestion API", description = "Rest APIs for the suggestion")
@ApiOperation(value = "User sign up API Endpoint")
@RestController
public class SuggestionController {

	@Autowired
	SuggestionService suggestionService;

	@Autowired
	SuggestionRepository suggestionRepository;
	
	@ApiOperation(value = "create suggestion", notes = " createTemplate", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CREATE_SUGGESTION })
	public ResponseEntity<TransactionInfo> CreateSuggestion(@RequestBody SuggestionRequest suggestionRequest) throws NotFoundException {

		Suggestion result = suggestionService.addSuggestions(suggestionRequest);

		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}
		
		throw new NotFoundException("No suggestion oufnd");

	}
	
	@ApiOperation(value = "create template", notes = " createTemplate", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_ALL_TEMPLATES })
	public ResponseEntity<TransactionInfo> getAllSuggestions(@RequestBody SuggestionFilter suggestionFilter) throws NotFoundException {

		List<SuggestionDTO> result = suggestionService.getAllSuggestions(suggestionFilter);

		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}
		
		throw new NotFoundException("No suggestion  found");

	}

	@PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_ALL_TEMPLATES })

	
	public ResponseEntity<TransactionInfo> updateSuggestionStatus(@RequestParam("suggestion_id") UUID suggestionId,
			@RequestBody Map<String, String> statusReq) {

		Suggestion suggestionEntity = suggestionService.findBySuggestionId(suggestionId,statusReq);

		if (suggestionEntity.getId() != null) {
			return ResponseBuilder.buildWithMessage("Status chnaged");
		}
		
		return ResponseBuilder.buildWithMessage("record_not_found");
	}

	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { GET_ALL_TEMPLATES })

	public ResponseEntity<TransactionInfo> deleteSuggestion(@RequestParam("suggestion_id") UUID suggestionId) {

		Suggestion suggestionEntity = suggestionRepository.findBySuggestionId(suggestionId);
			
		if (suggestionEntity != null) {

			suggestionRepository.delete(suggestionEntity);

			return ResponseBuilder.buildWithMessage("suggestion_deleted");
		}

		return ResponseBuilder.buildWithMessage("record_not_found");
	}

	@RequestMapping(value = "/export_excel", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> excelExport(HttpServletResponse response) {
		String filename = "suggestions.xlsx";
		List<Suggestion> suggestionDetails = suggestionRepository.findAll();
		ByteArrayInputStream stream = suggestionService.writeExcel(suggestionDetails, filename);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=suggestions.xlsx");
		InputStreamResource fileData = new InputStreamResource(stream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(fileData);

	}

	@RequestMapping(value = "/export_pdf", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> pdfExport() {
		String filename = "suggestions.pdf";
		List<Suggestion> suggestionDetails = suggestionRepository.findAll();
		ByteArrayInputStream bis = GeneratePdfReport.suggestionsPDF(suggestionDetails);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
	}

	@PostMapping(value = "/export_csv", produces = "text/csv")
	@SecuredWIthPermission(permissions = { GET_ALL_TEMPLATES })

	public ResponseEntity<InputStreamResource> exportCSV(HttpServletResponse response) throws Exception {
		String filename = "suggestions.csv";
		List<Suggestion> suggestionDetails = suggestionRepository.findAll();
		InputStreamResource fileData = new InputStreamResource(suggestionService.load(suggestionDetails));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(fileData);
	}
}
