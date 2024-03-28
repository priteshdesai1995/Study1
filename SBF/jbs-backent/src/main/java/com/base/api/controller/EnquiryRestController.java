package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CREATE_ENQUIRY_DATA;
import static com.base.api.constants.PermissionConstants.DELETE_ENQUIRY_DATA;
import static com.base.api.constants.PermissionConstants.GET_ALL_ENQUIRY_DATA;
import static com.base.api.constants.PermissionConstants.GET_ENQUIRY_DATA;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_ENQUIRY_DATA;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.Enquiry;
import com.base.api.exception.APIException;
import com.base.api.repository.EnquiryRepository;
import com.base.api.request.dto.EnquiryDto;
import com.base.api.service.EnquiryService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 * 
 *         This is the controller for the Enquire data APIs
 *
 */
@RequestMapping("/data")
@Api(tags = "Enquiry module API", description = "RESTApi for the Enquiry Module")
@Slf4j
@RestController
public class EnquiryRestController {

	@Autowired
	private EnquiryService enquiryService;

	@Autowired
	private EnquiryRepository enquiryRepo;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * @return
	 * @throws Exception
	 * 
	 *                   This ARestApi is used to get all data from Enquire module
	 */
	@GetMapping(value = "/allData")
	@ApiOperation(value = "API Endpoint to Get all the data", notes = PERMISSION
			+ GET_ALL_ENQUIRY_DATA, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_ENQUIRY_DATA })
	public ResponseEntity<TransactionInfo> getAllEnquiryData(@RequestBody EnquiryDto enquiryDto) throws Exception {

		log.info("API hit of all the data");
		List<Enquiry> listOfData = enquiryService.getAllEnquiryData(enquiryDto);
		return ResponseBuilder.buildCRUDResponse(listOfData, resourceBundle.getString("enquiry-list"), HttpStatus.OK);
	}

	/**
	 * @param enquiryId
	 * @return
	 * @throws Exception
	 * 
	 *                   This RestApi is used to get the particular data based on
	 *                   id.
	 */
	@GetMapping(value = "/getData/{enquiryId}")
	@ApiOperation(value = "API Endpoint to Get the data", notes = PERMISSION + GET_ENQUIRY_DATA, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ENQUIRY_DATA })
	public ResponseEntity<TransactionInfo> getEnquiryData(@RequestParam("enquiryId") UUID enquiryId) throws Exception {

		log.info("API hit of the get data");
		Optional<Enquiry> data = enquiryRepo.findById(enquiryId);
		if (!data.isPresent()) {
			return ResponseBuilder.buildRecordNotFoundResponse(data, resourceBundle.getString("record_not_found"));
		}
		Enquiry enquiryData = enquiryService.getEnquiryDataById(enquiryId);
		log.info("Get the data successfully");
		return ResponseBuilder.buildCRUDResponse(enquiryData, resourceBundle.getString("enquiry-list-by-id"),
				HttpStatus.OK);
	}

	/**
	 * @param enquiryDto
	 * @return
	 * @throws Exception
	 * 
	 *                   This RestApi is used create new Data
	 */
	@SuppressWarnings("null")
	@PostMapping(value = "/create")
	@ApiOperation(value = "API Endpoint to create the data", notes = PERMISSION
			+ CREATE_ENQUIRY_DATA, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_ENQUIRY_DATA })
	public ResponseEntity<TransactionInfo> createEnquiryData(@Valid @RequestBody EnquiryDto enquiryDto)
			throws Exception {
		log.info("API hit for the create data");

		Enquiry newData = new Enquiry(enquiryDto);
		Enquiry createdData = enquiryRepo.save(newData);
		log.info("newData is created" + newData.toString());
		if (null != createdData || null != createdData.getUuid()) {
			log.info("Create Data successfull");
		} else {
			log.error("not able to create data");
			return ResponseBuilder.buildRecordNotFoundResponse(createdData,
					resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildCRUDResponse(createdData, resourceBundle.getString("create-enquiry"),
				HttpStatus.OK);
	}

	/**
	 * @param enquiryDto
	 * @param enquiryId
	 * @return
	 * @throws Exception
	 * 
	 *                   This RestApi is used to update the data based on id
	 */
	@SuppressWarnings("unused")
	@PutMapping(value = "/updateData/{enquiryId}")
	@ApiOperation(value = "API Endpoint to Update the Data", notes = PERMISSION
			+ UPDATE_ENQUIRY_DATA, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_ENQUIRY_DATA })
	public ResponseEntity<TransactionInfo> updateEnquiryData(@Valid @RequestBody EnquiryDto enquiryDto,
			@RequestParam("enquiryId") UUID enquiryId) throws Exception {
		log.info("API hit of the update data");
		Optional<Enquiry> data = enquiryRepo.findById(enquiryId);
		Enquiry updatedData = data.get();
		if (data == null) {
			log.error("Not able to fetch data");
			throw new APIException("not.able.to.fetch.data", HttpStatus.NOT_FOUND);
		}
		updatedData.update(enquiryDto);
		Enquiry enquire = enquiryRepo.save(updatedData);
		log.info("Enquiry data updated successfully..!");
		return ResponseBuilder.buildCRUDResponse(enquire, resourceBundle.getString("update-enquiry"), HttpStatus.OK);
	}

	/**
	 * @param enquiryId
	 * @return
	 * @throws Exception
	 * 
	 *                   This RestApi is used to delete the data based on id
	 */
	@DeleteMapping(value = "/deleteData/{enquiryId}")
	@ApiOperation(value = "API Endpoint to Delete the data", notes = PERMISSION
			+ DELETE_ENQUIRY_DATA, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_ENQUIRY_DATA })
	public ResponseEntity<TransactionInfo> deleteEnquiryData(@PathVariable UUID enquiryId) throws Exception {

		log.info("API hit of the delete data");
		Optional<Enquiry> deleteData = enquiryRepo.findById(enquiryId);
		if (deleteData == null) {
			log.error("no data found");
			return ResponseBuilder.buildRecordNotFoundResponse(deleteData, resourceBundle.getString("record_not_found"));
		}
		enquiryService.deleteEnquiryDataById(enquiryId);
		log.info("Delete the data successfully");
		return ResponseBuilder.buildCRUDResponse(deleteData, resourceBundle.getString("delete-enquiry"), HttpStatus.OK);
	}

}
