package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.CHANGE_OFFER_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_OFFER;
import static com.base.api.constants.PermissionConstants.DELETE_OFFER;
import static com.base.api.constants.PermissionConstants.EXPORT_TO_CSV;
import static com.base.api.constants.PermissionConstants.EXPORT_TO_EXCEL;
import static com.base.api.constants.PermissionConstants.EXPORT_TO_PDF;
import static com.base.api.constants.PermissionConstants.GET_ACTIVE_USERS;
import static com.base.api.constants.PermissionConstants.GET_ALL_OFFERS;
import static com.base.api.constants.PermissionConstants.GET_OFFER;
import static com.base.api.constants.PermissionConstants.UPDATE_OFFER;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.config.GeneratePdfReport;
import com.base.api.constants.Constants;
import com.base.api.dto.filter.OfferFilter;
import com.base.api.entities.Offer;
import com.base.api.entities.User;
import com.base.api.enums.UserStatus;
import com.base.api.exception.APIException;
import com.base.api.repository.OfferRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.OfferDTO;
import com.base.api.service.OfferService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller contains API End points for Offer Module.
 * 
 * @author kavitha_deshagani
 *
 */
@RestController
@Slf4j
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    UserRepository userRepository;
	
    /**
     * This End point creates the offers.
     * 
     * @param offerFilter request object
     * @return String message
     * @throws Exception
     */
    @PostMapping(value = "/store", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "To create an offer", notes = PERMISSION + CREATE_OFFER, authorizations = {
            @Authorization(value = AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { CREATE_OFFER })
    public ResponseEntity<TransactionInfo> createOffer(@RequestBody OfferFilter offerFilter) {

        log.info("OfferController : createOffer");

        offerService.createOffer(offerFilter);

        log.info("Offer Created succeessfully");

        return ResponseBuilder.buildWithMessage("offer data created successfully");
    }

    /**
     * This End point get the all offers
     * 
     * @param offerFilter request object
     * @return list of offers
     * @throws Exception
     */
    @PostMapping(value = "/list", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "To  get all offer", notes = PERMISSION + GET_ALL_OFFERS, authorizations = {
            @Authorization(value = AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { GET_ALL_OFFERS })
    public ResponseEntity<TransactionInfo> getAllOffers(@RequestBody OfferFilter offerFilter) throws Exception {

        log.info("OfferController : getAllOffer");

        List<Offer> result = offerService.getAllOffers(offerFilter);

        log.info("OfferController : getAlloffer Successfully");

        return ResponseBuilder.buildOkResponse(result);
    }

    /**
     * This End point is used to get the offer by id.
     * 
     * @param offerId
     * @return offer based on offerId
     * @throws Exception
     */
    @GetMapping(value = "/show", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "To  get offer by id", notes = PERMISSION + GET_OFFER, authorizations = {
            @Authorization(value = AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { GET_OFFER })
    public ResponseEntity<TransactionInfo> getOffer(@RequestParam("offer_id") UUID offerId) throws Exception {

        log.info("OfferController : getOffer");

        OfferDTO result = offerService.getOffer(offerId);

        if (result == null) {
            log.error("offer not found");
            throw new APIException("offer.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("OfferController : getOffer successfully");

        return ResponseBuilder.buildOkResponse(result);
    }

    /**
     * This End point is used to update the data
     * 
     * @param offerFilter
     * @param offerId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @PutMapping(value = "/update", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "API Endpoint to update Offer by Id", notes = PERMISSION + UPDATE_OFFER, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { UPDATE_OFFER })
    public ResponseEntity<TransactionInfo> updateOffer(@RequestParam("offer_id") UUID offerId,
            @Valid @RequestBody OfferFilter offerFilter) throws Exception {

        log.info("OfferController : updateOffer");

        String result = offerService.updateOffer(offerId, offerFilter);

        log.info("OfferController : updateOffer successfully");

        return ResponseBuilder.buildOkResponse(result);

    }

    /**
     * This End point is used to delete the data
     * 
     * @param offerId
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "API Endpoint to Delete the data", notes = PERMISSION + DELETE_OFFER, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { DELETE_OFFER })
    public ResponseEntity<TransactionInfo> deleteOffer(@RequestParam("offer_id") UUID offerId) throws Exception {

        log.info("OfferController : deleteOffer");

        String result = offerService.deleteOffer(offerId);

        log.info("OfferController : updateOffer successfully");

        return ResponseBuilder.buildOkResponse(result);
    }

    @GetMapping(value = "/getActiveUserList", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "API Endpoint to get the list of Active user data", notes = PERMISSION
            + GET_ACTIVE_USERS, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { GET_ACTIVE_USERS })
    public ResponseEntity<TransactionInfo> getActiveUsers() {

        log.info("OfferController : getActiveUsers");

        List<User> result = userRepository.findByStatus(UserStatus.ACTIVE);

        if (result == null) {
            log.error("User not found");
            throw new APIException("User.not.found", HttpStatus.NOT_FOUND);
        }
        log.info("OfferController : getActiveUsers successfully");

        return ResponseBuilder.buildOkResponse(result);
    }

    @PutMapping(value = "/change_status")
    @ApiOperation(value = "API Endpoint to change the status of the offer ", notes = PERMISSION
            + CHANGE_OFFER_STATUS, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { CHANGE_OFFER_STATUS })
    public ResponseEntity<TransactionInfo> changeOfferStatus(@RequestBody Map<String, String> statusReq) {

        log.info("OfferController : changeOfferStatus");

        offerService.changeStatus(statusReq);

        log.info("OfferController : status changed successfully");

        return ResponseBuilder.buildWithMessage("Status changed successfully");
    }

    @PostMapping(value = "/export_excel")
    @ApiOperation(value = "API Endpoint to export data of the offer into excel file", notes = PERMISSION
            + EXPORT_TO_EXCEL, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { EXPORT_TO_EXCEL })
    public ResponseEntity<InputStreamResource> excelExport(HttpServletResponse response) {

        log.info("OfferController : excelExport");

        String filename = "offers.xlsx";

        List<Offer> offers = offerRepository.findAll();
        ByteArrayInputStream stream = offerService.writeExcel(offers, filename);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=offers.xlsx");

        InputStreamResource fileData = new InputStreamResource(stream);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(fileData);

    }

    @PostMapping(value = "/export_pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ApiOperation(value = "API Endpoint to export data of the offer into pdf file ", notes = PERMISSION
            + EXPORT_TO_PDF, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { EXPORT_TO_PDF })
    public ResponseEntity<InputStreamResource> pdfExport() {

        log.info("offerController : pdfExport");

        String filename = "offers.pdf";

        List<Offer> offers = offerRepository.findAll();

        ByteArrayInputStream bis = GeneratePdfReport.offersPDF(offers);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
    }

    @PostMapping(value = "/export_csv", produces = "text/csv")
    @ApiOperation(value = "API Endpoint to export data of the offer into csv file", notes = PERMISSION
            + EXPORT_TO_CSV, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { EXPORT_TO_CSV })
    public ResponseEntity<InputStreamResource> exportCSV(HttpServletResponse response) throws Exception {

        log.info("offerController : exportCSV");

        String filename = "offers.csv";

        List<Offer> offers = offerRepository.findAll();

        InputStreamResource fileData = new InputStreamResource(offerService.load(offers));

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv")).body(fileData);
    }

}
