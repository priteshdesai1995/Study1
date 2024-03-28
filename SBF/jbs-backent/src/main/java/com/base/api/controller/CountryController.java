package com.base.api.controller;

import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.ADD_COUNTRY;
import static com.base.api.constants.PermissionConstants.CHANGE_COUNTRY_STATUS;
import static com.base.api.constants.PermissionConstants.DELETE_COUNTRY_BY_ID;
import static com.base.api.constants.PermissionConstants.GET_ALL_COUNTRIES;
import static com.base.api.constants.PermissionConstants.GET_COUNTRY_BY_ID;
import static com.base.api.constants.PermissionConstants.SEARCH_COUNTRY;
import static com.base.api.constants.PermissionConstants.UPDATE_COUNTRY_BY_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.Country;
import com.base.api.filter.CountryFilter;
import com.base.api.repository.CountryRepository;
import com.base.api.request.dto.CountryDTO;
import com.base.api.request.dto.CountryTranslationDTO;
import com.base.api.service.CountryService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kavitha_deshagani
 *
 */
@RestController
@RequestMapping("/country")
@Slf4j
public class CountryController {

    @Autowired
    CountryService countryService;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    ResourceBundle resourceBundle;

    /**
     * @param countryDTO
     * @return This API is used to create the new country
     */
    @PostMapping(value = "/create")
    @ApiOperation(value = "API Endpoint to add the Country", notes = PERMISSION + ADD_COUNTRY, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { ADD_COUNTRY })
    public ResponseEntity<TransactionInfo> createCountry(@Valid @RequestBody CountryDTO countryDTO) {
        log.info("Country create call starts");
        String result = countryService.createCountry(countryDTO);
        if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
            log.info("Country saved succesfully");
            return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("country_created"),
                    HttpStatus.CREATED);
        }
        return ResponseBuilder.buildInternalServerErrorResponse(result,
                resourceBundle.getString("country_create_fail"));
    }

    /**
     * @param statusReq
     * @return This Rest API is used to change the status
     */
    @PutMapping(value = "/changeStatus")
    @ApiOperation(value = "API Endpoint to update Country by Id", notes = PERMISSION
            + SEARCH_COUNTRY, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { CHANGE_COUNTRY_STATUS })
    public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {
        log.info("Country change status call starts");
        UUID uuid = UUID.fromString(statusReq.get("uuid"));
        String status = statusReq.get("status");
        String result = countryService.changeStatus(uuid, status);
        log.info("Country change status call ends");

        if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

            return ResponseBuilder.buildStatusChangeResponse(result, resourceBundle.getString("status"));
        }

        return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
    }

    /**
     * @return This API is used to get the all countries data
     */
    @GetMapping(value = "/getAllCountries")
    @ApiOperation(value = "API Endpoint to list all countries", notes = PERMISSION + SEARCH_COUNTRY, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { GET_ALL_COUNTRIES })
    public ResponseEntity<TransactionInfo> getAllCountries() {
        log.info("CountryController : getAllCountries");
        List<CountryTranslationDTO> countries = countryService.getAllCountries();
        if (countries == null) {
            return ResponseBuilder.buildRecordNotFoundResponse(countries, resourceBundle.getString("record_not_found"));
        }
        log.info("getAllCountri");
        return ResponseBuilder.buildOkResponse(countries);
    }

    /**
     * @param uuid
     * @return This API is used to get the country based on id
     */
    @GetMapping(value = "/get/{uuid}")
    @ApiOperation(value = "API Endpoint to Get Country by Id", notes = PERMISSION + SEARCH_COUNTRY, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { GET_COUNTRY_BY_ID })
    public ResponseEntity<TransactionInfo> getCountry(@Valid @PathVariable("uuid") UUID countryId) {
        log.info("Country get by id call starts");
        Optional<CountryDTO> countryData = countryService.getCountryById(countryId);
        if (!countryData.isPresent()) {
            return ResponseBuilder.buildRecordNotFoundResponse(countryData,
                    resourceBundle.getString("record_not_found"));
        }
        log.info("Get the country successfully..");
        return ResponseBuilder.buildOkResponse(countryData);
    }

    /**
     * @param countryDTO
     * @param uuid
     * @return This API is used to update the data based on id.
     */
    @PutMapping(value = "/update/{uuid}")
    @ApiOperation(value = "API Endpoint to update Country by Id", notes = PERMISSION
            + SEARCH_COUNTRY, authorizations = { @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { UPDATE_COUNTRY_BY_ID })
    public ResponseEntity<TransactionInfo> updateCountry(@RequestBody CountryDTO countryDTO,
            @PathVariable("uuid") UUID uuid) {
        log.info("Country update call starts");
        String result = countryService.updateCountry(countryDTO, uuid);
        log.info("Country update call ends");
        if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
            return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("country_updated"), HttpStatus.OK);
        } else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {
            return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
        }

        return ResponseBuilder.buildInternalServerErrorResponse(result,
                resourceBundle.getString("country_update_fail"));
    }

    /**
     * @param countryFilter
     * @return This API is used to search the country data
     */
    @PostMapping(value = "/search")
    @ApiOperation(value = "API Endpoint to search the Country", notes = PERMISSION + SEARCH_COUNTRY, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { SEARCH_COUNTRY })
    public ResponseEntity<TransactionInfo> searchCountry(@RequestBody CountryFilter countryFilter) {
        log.info("Country search call starts");
        List<Country> countries = new ArrayList<Country>();
        log.info("list of countries");
        countries = countryService.searchCountry(countryFilter);
        int count = countryService.getTotalCount(countryFilter);
        log.info("Country search call ends");
        return ResponseBuilder.buildSearchFileterResponse(countries, "Country Search Result", count);

    }

    
   
    /**
     * Delete country.
     *
     * @param uuid the uuid
     * @return the response entity
     */
    @DeleteMapping(value = "/delete/{uuid}")
    @ApiOperation(value = "API Endpoint to delete the Country", notes = PERMISSION + DELETE_COUNTRY_BY_ID, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { DELETE_COUNTRY_BY_ID })
    public ResponseEntity<TransactionInfo> deleteCountry(@PathVariable("uuid") UUID uuid) {
        log.info("Country DELETE call starts");
        String result = null;
        result = countryService.deleteCountry(uuid);
        if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

            log.info("Country DELETE call ends");
            return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("category_deleted"),
                    HttpStatus.OK);
        } else if (result.equalsIgnoreCase(HttpStatus.NOT_FOUND.name())) {

            log.info("Country DELETE call ends");
            return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("category_delete_fail"));
        }


        log.info("Country DELETE call ends");
        return ResponseBuilder.buildInternalServerErrorResponse(result,
                resourceBundle.getString("category_delete_fail"));
    }
}
