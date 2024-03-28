package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CREATE_MEDIA;
import static com.base.api.constants.PermissionConstants.GET_ALL_MEDIA;
import static com.base.api.constants.PermissionConstants.DELETE_MEDIA;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.CREATE_FOLDER;
import static com.base.api.constants.PermissionConstants.RENAME_MEDIA;
import static com.base.api.constants.PermissionConstants.MOVE_FOLDER;

import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.dto.filter.MediaFilter;
import com.base.api.entities.Media;
import com.base.api.request.dto.FolderRequestDTO;
import com.base.api.request.dto.MediaDTO;
import com.base.api.request.dto.MoveFolderRequestDTO;
import com.base.api.request.dto.RenameFolderRequestDTO;
import com.base.api.service.MediaService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/media")
public class MediaManagerController {
	
	@Autowired
	MediaService mediaService;
	
	@Autowired
	ResourceBundle resourceBundle;
	
	@ApiOperation(value = "create media", notes = " createMedia", authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@PostMapping(value = "/create", headers = {
			"Version=V1" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@SecuredWIthPermission(permissions = { CREATE_MEDIA })
	public ResponseEntity<TransactionInfo> createMedia(@Valid @RequestPart MediaDTO mediaDTO,
			@RequestPart MultipartFile file) {
		
		log.info("MediaController : CreateMedia()");
		
		String result = mediaService.createMedia(mediaDTO, file);
		
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Media created Successfully");
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("media-created"),
					HttpStatus.CREATED);
		}
		log.error("Media not Found");
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("media-created-fail"));
	}
	
	@PostMapping(value = "/search", headers = {"Version=V1" }, 
			consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get list of the media", notes = PERMISSION
			+ GET_ALL_MEDIA, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_ALL_MEDIA })
	public ResponseEntity<TransactionInfo> searchMedia(@RequestBody MediaFilter mediaFilter) {
		
		log.info("MediaController : searchMedia");
		
		List<Media> result = mediaService.searchMedia(mediaFilter);
		
		if(result != null) {
			log.info("MediaController : list get successfully");
			return ResponseBuilder.buildOkResponse(result);
		}
		log.error("Media not found");
		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("media-list-fail"));
	}
	
	@DeleteMapping(value = "/delete/{mediaId}", headers = { "Version=V1" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to delete media", notes = PERMISSION
	+ DELETE_MEDIA, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_MEDIA })
	public ResponseEntity<TransactionInfo> deleteMedia(@PathVariable("mediaId") UUID mediaId) {
		
		log.info("MediaManagerController : deleteMedia");
		
		String result = mediaService.deleteMedia(mediaId);
		
		if (result.equals(HttpStatus.OK.name())) {
			log.info("Media deleted Successfully");
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("media_deleted"), HttpStatus.OK);	
		}
		else if(result.equals(HttpStatus.NOT_FOUND.name())) {
			log.info("Media not Found");
			return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
		}
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("media_delete_fail"));
	}

	
	@PostMapping(value = "/create-folder", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to create folder", notes = PERMISSION
	+ CREATE_FOLDER, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CREATE_FOLDER })
	public ResponseEntity<TransactionInfo> createFolder(@RequestBody FolderRequestDTO requestDTO) {
		
		log.info("MediaManagerController : createFolder");
		
		String result = mediaService.createFolder(requestDTO);
		
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			log.info("Folder created successfully");
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("folder-created"),
					HttpStatus.CREATED);
		}
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("folder-created-fail"));
	}

	@PostMapping(value = "/rename-media", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to rename media ", notes = PERMISSION
	+ RENAME_MEDIA, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { RENAME_MEDIA })
	public ResponseEntity<TransactionInfo> renameFolder(@RequestBody RenameFolderRequestDTO requestDTO) {
		String result = mediaService.renameFolderOrFile(requestDTO);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("rename-file-successfully"),
					HttpStatus.CREATED);
		}
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("rename-file-fail"));
	}

	@PostMapping(value = "/move-media", headers = { "Version=V1" }, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to move folder", notes = PERMISSION
	+ MOVE_FOLDER, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { MOVE_FOLDER })
	public ResponseEntity<TransactionInfo> moveFolder(@RequestBody MoveFolderRequestDTO requestDTO) {
		String result = mediaService.moveFolder(requestDTO);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("move-media-successfully"),
					HttpStatus.CREATED);
		}
		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("move-media-fail"));
	}

	

	
}
