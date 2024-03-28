package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.BANNER_LIST;
import static com.base.api.constants.PermissionConstants.CHANGE_BANNER_STATUS;
import static com.base.api.constants.PermissionConstants.CREATE_BANNER;
import static com.base.api.constants.PermissionConstants.DELETE_BANNER;
import static com.base.api.constants.PermissionConstants.GET_BANNER;
import static com.base.api.constants.PermissionConstants.UPDATE_BANNER;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.dto.filter.BannerFilter;
import com.base.api.entities.Banner;
import com.base.api.service.BannerService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/banner")
public class BannerController {

	@Autowired
	private BannerService bannerService;

	@Autowired
	private ResourceBundle resourceBundle;

	/**
	 * 
	 * This is the controller that will return the list of all banners with filter
	 * 
	 * This is get method but we need to use PostMapping because we need to pass
	 * request body
	 * 
	 * @return
	 * @throws NotFoundException
	 */
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get banner list", notes = PERMISSION + BANNER_LIST, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { BANNER_LIST })
	public ResponseEntity<TransactionInfo> getBanners(@RequestBody BannerFilter bannerFilter) throws NotFoundException {

		List<Banner> bannerList = bannerService.getBannerList(bannerFilter);

		if (bannerList == null) {
			log.error("banner list is empty");
			throw new NotFoundException("Banner Not Found", null);
		}

		return ResponseBuilder.buildOkResponse(bannerList);
	}

	/**
	 * 
	 * This is the controller which will create banner using banner filter
	 * 
	 * @param bannerFilter
	 * @return
	 */
	@PostMapping(value = "/store",produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "create a banner by passing banner filter", notes = PERMISSION
			+ CREATE_BANNER, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_BANNER })
	public ResponseEntity<TransactionInfo> createBanner(@RequestBody BannerFilter bannerFilter){

		String result = bannerService.createBanner(bannerFilter);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Banner created successfully");

			return ResponseBuilder.buildCRUDResponse(result,resourceBundle.getString("banner_created"), HttpStatus.OK);
		} else {
			log.error("Banner not created successfully.");
			return ResponseBuilder.buildInternalServerErrorResponse(result,resourceBundle.getString("banner_create_fail"));
		}
	}

	/**
	 * 
	 * This is the controller method that will change the status of the banner
	 * 
	 * @return
	 */
	@PostMapping(value = "/change_status", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Change the status of the banner", notes = PERMISSION
			+ CHANGE_BANNER_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = CHANGE_BANNER_STATUS)
	public ResponseEntity<TransactionInfo> changeStatus(@RequestBody Map<String, String> statusReq) {
		String result = bannerService.changeStatus(statusReq);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Banner status changed successfully");
			return ResponseBuilder.buildStatusChangeResponse(result,
					resourceBundle.getString("banner_status_changed_successfully"));
		} else {
			log.error("Banner not found");
			return ResponseBuilder.buildRecordNotFoundResponse(result,
					resourceBundle.getString("banner_status_can_not_be_changed"));
		}
	}

	/**
	 * 
	 * This Controller method will return single bnner by id
	 * 
	 * @param banner_id
	 * @return
	 */
	@GetMapping(value = "/show", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get the banner by id ", notes = PERMISSION + GET_BANNER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = GET_BANNER)
	public ResponseEntity<TransactionInfo> getBanner(@QueryParam("banner_id") String banner_id) {
		
		Banner banner = bannerService.getBanner(UUID.fromString(banner_id));

		return ResponseBuilder.buildOkResponse(banner);
	}

	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "update the banner by id ", notes = PERMISSION + UPDATE_BANNER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = UPDATE_BANNER)
	public ResponseEntity<TransactionInfo> updateBanner(@RequestParam("banner_id") UUID banner_id,
			@RequestBody BannerFilter bannerFilter) {

		String result = bannerService.updateBanner(banner_id, bannerFilter);

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Banner updated successfully");

			return ResponseBuilder.buildCRUDResponse(result,
					resourceBundle.getString("banner_updated"), HttpStatus.OK);
		} else {
			log.error("Banner not updated");
			return ResponseBuilder.buildInternalServerErrorResponse(result,
					resourceBundle.getString("banner_update_fail"));
		}

	}

	@DeleteMapping(value = "/delete",  produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "delete the banner by id ", notes = PERMISSION + DELETE_BANNER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = DELETE_BANNER)
	public ResponseEntity<TransactionInfo> deleteBanner(@QueryParam("banner_id") String banner_id) {

		String result = bannerService.deleteBanner(UUID.fromString(banner_id));

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Banner deleted successfully");

			return ResponseBuilder.buildCRUDResponse(result,
					resourceBundle.getString("banner_deleted"), HttpStatus.OK);
		} else {
			log.error("Banner can not be deleted, some error occured.");
			return ResponseBuilder.buildRecordNotFoundResponse(result,
					resourceBundle.getString("banner_delete_fail"));
		}
	}
}
