/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.ANNOUNCEMENT_MANAGE;
import static com.base.api.constants.PermissionConstants.ANNOUNCEMENT_VIEW;

import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.dto.filter.AnnouncementFilter;
import com.base.api.entities.Announcement;
import com.base.api.entities.AnnouncementUser;
import com.base.api.entities.User;
import com.base.api.repository.AnnouncementUserRepository;
import com.base.api.repository.UserRepository;
import com.base.api.service.AnnouncementService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller contains API Endpoints to manage Announcement.
 * 
 * @author minesh_prajapati
 *
 */

@Slf4j
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

	@Autowired
	AnnouncementService announcementService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AnnouncementUserRepository announcementUserRepository;
	
	
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list of announcements", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
	public ResponseEntity<TransactionInfo> getAnnouncements(@RequestBody AnnouncementFilter announcementFilter) {
		List<Announcement> result = announcementService.getAnnouncements(announcementFilter);
		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}
	
	
	@GetMapping(value = "/show", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get single announcement", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
	public ResponseEntity<TransactionInfo> getAnnouncement(@RequestParam("announcement_id") String announcement_id) {
		Announcement result = announcementService.getAnnouncement(announcement_id);

		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get single announcement", notes = PERMISSION + ANNOUNCEMENT_MANAGE, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_MANAGE })
	public ResponseEntity<TransactionInfo> createAnnouncement(@RequestBody AnnouncementFilter announcementFilter) throws ParseException {

		log.info("Create announcement : AnnouncementController");
		
		String result = announcementService.createAnnouncement(announcementFilter);
		if (result.equals(HttpStatus.OK.name())) {
			log.info("Announcement created");
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("announcement_created"),
					HttpStatus.CREATED);
		}
		log.info("Announcement creation failed");
		return ResponseBuilder.buildInternalServerErrorResponse(result,
				resourceBundle.getString("announcement_create_fail"));
	}
	
	@PostMapping(value = "/user_selection_list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get single announcement", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
	public ResponseEntity<TransactionInfo> getUsersList(@RequestBody AnnouncementFilter announcementFilter) {
		List<User> result = userRepository.findAll();

		if (result != null) {
			return ResponseBuilder.buildOkResponse(result);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}
	

	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get single announcement", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
	public ResponseEntity<TransactionInfo> getUsers(@QueryParam("announcement_id") String announcement_id) {
		 List<Announcement> result = announcementUserRepository.findByAnnouncementId(announcement_id);

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * This endpoint creates Announcement and assign user.
	 * 
	 * @param attachmentFile  multipart File.
	 * @param announcementDTO request object.
	 * @return string message.
	 */
//	@ApiOperation(value = "To create an announcement", notes = PERMISSION + ANNOUNCEMENT_MANAGE, authorizations = {
//			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_MANAGE })
//	@PostMapping(value = "/create", headers = {
//			"Version=V1" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<TransactionInfo> createAnnouncement(@RequestPart MultipartFile attachmentFile,
//			@Valid @RequestPart AnnouncementDTO announcementDTO) {
//		log.info("AnnouncementController: createAnnouncement");
//		Announcement result = announcementService.createAnnouncement(attachmentFile, announcementDTO);
//		if (result != null) {
//			log.info("AnnouncementController: createAnnouncement successful");
//			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("announcement_created"),
//					HttpStatus.CREATED);
//		}
//
//		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
//	}

	/**
	 * This endpoint get all announcement.
	 * 
	 * @param announcementDTO request dto.
	 * @return list of announcement.
	 */
//	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(value = "To view all the announcements", notes = PERMISSION + ANNOUNCEMENT_MANAGE + SPACE
//			+ ANNOUNCEMENT_VIEW, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_MANAGE, ANNOUNCEMENT_VIEW })
//	public ResponseEntity<TransactionInfo> getAnnouncements(@RequestBody AnnouncementDTO announcementDTO) {
//	
//		log.info("AnnouncementController: getAnnouncements");
//		
//		Page<Announcement> result = announcementService.getAllAnnouncements(announcementDTO);
//		if (result != null) {
//			return ResponseBuilder.buildOkResponse(result);
//		}
//		log.info("AnnouncementController: getAnnouncements successful");
//		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
//	}

	/**
	 * This endpoint get announcement by id.
	 * 
	 * @param announcementId uuid of announcement.
	 * @return obj of announcement.
	 */
//	@ApiOperation(value = "To view the announcement", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
//			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
//	@GetMapping(value = "/show", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<TransactionInfo> getAnnouncementById(@RequestParam("announcementId") UUID announcementId) {
//		log.info("AnnouncementController: getAnnouncementById {}", announcementId);
//		Announcement announcement = announcementService.getAnnouncementById(announcementId);
//		if (announcement != null) {
//			// start for create attachment view/download link
//			if (announcement.getType().equals(TemplateType.EMAIL) && !Util.isEmpty(announcement.getAttachment())) {
//				String[] path = StringUtil.splitByForwardSlash(announcement.getAttachment());
//				String url = MvcUriComponentsBuilder
//						.fromMethodName(AnnouncementController.class, "getAnnouncemenAttachmentFile", path[0], path[1])
//						.build().toString();
//				announcement.setAttachment(url);
//			}
//			// end for create attachment view/download link
//			log.info("AnnouncementController: getAnnouncementById successful {}", announcementId);
//			return ResponseBuilder.buildOkResponse(announcement);
//		}
//		return ResponseBuilder.buildRecordNotFoundResponse(announcement, resourceBundle.getString("record_not_found"));
//	}

	/**
	 * This endpoint get announcement users by id.
	 * 
	 * @param announcementId        uuid of announcement.
	 * @param orderAndPaginationDTO request Object for searching, sorting,
	 *                              pagination.
	 * @return object of announcement.
	 */
//	@ApiOperation(value = "To view the announcement's users", notes = PERMISSION + ANNOUNCEMENT_VIEW, authorizations = {
//			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
//	@PostMapping(value = "/show/users", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<TransactionInfo> getAnnouncementUsersById(@RequestParam("announcementId") UUID announcementId,
//			@RequestBody OrderAndPaginationDTO orderAndPaginationDTO) {
//		log.info("AnnouncementController: getAnnouncementUsersById {}", announcementId);
//		Page<UserResponseDTO> users = announcementService.getAnnouncementUsersById(announcementId,
//				orderAndPaginationDTO);
//		log.info("AnnouncementController: getAnnouncementUsersById successful {}", announcementId);
//		return ResponseBuilder.buildOkResponse(users);
//	}

	/**
	 * This endpoint get announcement attachment.
	 * 
	 * @param dirId    dynamic directory name.
	 * @param fileName file name.
	 * @return file object.
	 */
//	@ApiOperation(value = "To view the announcement's attachment", notes = PERMISSION
//			+ ANNOUNCEMENT_VIEW, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { ANNOUNCEMENT_VIEW })
//	@GetMapping(value = "/{dirId}/{fileName}")
//	public ResponseEntity<Resource> getAnnouncemenAttachmentFile(@PathVariable String dirId,
//			@PathVariable String fileName) {
//		log.info("AnnouncementController: getAnnouncemenAttachmentFile {} {}", dirId, fileName);
//		Resource attachmentFile = announcementService.getAnnouncemenAttachmentFile(dirId, fileName);
//		log.info("AnnouncementController: getAnnouncemenAttachmentFile successful {} {}", dirId, fileName);
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//				"attachment; filename=\"" + attachmentFile.getFilename() + "\"").body(attachmentFile);
//	}
}
