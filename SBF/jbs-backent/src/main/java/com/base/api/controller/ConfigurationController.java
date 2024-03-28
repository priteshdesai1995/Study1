package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.GET_CONFIGURATION;
import static com.base.api.constants.PermissionConstants.GET_LOGO;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.UPDATE_LOGO;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.entities.Configuration;
import com.base.api.entities.Logo;
import com.base.api.repository.LogoRepository;
import com.base.api.service.ConfigurationService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {
	@Autowired
	ConfigurationService configurationService;

	@Autowired
	LogoRepository logoRepository;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Configuration list.
	 *
	 * @param request the request
	 * @return the response entity
	 * @throws ParseException the parse exception
	 */
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to get Configuration", notes = PERMISSION
			+ GET_CONFIGURATION, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_CONFIGURATION })
	public ResponseEntity<TransactionInfo> configurationList(@RequestBody Map<String, String> request)
			throws ParseException {

		List<Configuration> result = configurationService.getConfigurations();

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Gets the logo.
	 *
	 * @return the logo
	 * @throws ParseException the parse exception
	 */
	@GetMapping(value = "/logo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get Configuration Logo", notes = PERMISSION + GET_LOGO, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_LOGO })
	public ResponseEntity<TransactionInfo> getLogo() throws ParseException {

		Logo result = configurationService.getLogo();

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	@GetMapping(value = "/logo/{siteName}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get Configuration Logo", notes = PERMISSION + GET_LOGO, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_LOGO })
	public ResponseEntity<TransactionInfo> getLogo(@PathVariable("siteName") String siteName) throws ParseException {

		List<Logo> result = configurationService.getSiteLogo(siteName);

		if (result != null) {

			return ResponseBuilder.buildOkResponse(result);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Update configuration.
	 *
	 * @param request the request
	 * @return the response entity
	 * @throws ParseException the parse exception
	 */
//	@PostMapping(value = "/update")
//	@ApiOperation(value = "API Endpoint to Update Configuration", notes = PERMISSION + UPDATE_LOGO, authorizations = {
//			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { UPDATE_LOGO })
//	public ResponseEntity<TransactionInfo> updateConfiguration(@RequestBody Map<String, String> request,
//			@RequestPart MultipartFile icon, @RequestPart MultipartFile favIcon) throws ParseException {
//
//		String result = configurationService.updateConfiguration(request, icon, favIcon);
//
//		if (result.equals(HttpStatus.OK.name())) {
//
//			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("setting_updated"),
//					HttpStatus.OK);
//		}
//
//		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
//	}

	@PostMapping(value = "/update")
	@ApiOperation(value = "API Endpoint to Update Configuration", notes = PERMISSION + UPDATE_LOGO, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_LOGO })
	public ResponseEntity<TransactionInfo> updateConfiguration(@RequestBody Map<String, String> request)
			throws ParseException {

		String result = configurationService.updateConfiguration(request);

		if (result.equals(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("setting_updated"),
					HttpStatus.OK);
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Delete image.
	 *
	 * @param imageId the image id
	 * @return the response entity
	 * @throws ParseException the parse exception
	 */
	@GetMapping(value = "/delete-image/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Get Configuration Logo", notes = PERMISSION + GET_LOGO, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_LOGO })
	public ResponseEntity<TransactionInfo> delteImage(@PathVariable("imageId") UUID imageId) throws ParseException {

		try {
			logoRepository.deleteById(imageId);
			return ResponseBuilder.buildOkResponse(imageId);
		} catch (Exception e) {
			return ResponseBuilder.buildRecordNotFoundResponse(imageId, resourceBundle.getString("record_not_found"));
		}
	}

	/**
	 * Update image.
	 *
	 * @param imageId the image id
	 * @return the response entity
	 * @throws ParseException the parse exception
	 */
	@PostMapping(value = "/updateimage/{siteName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ApiOperation(value = "API Endpoint to Update Configuration Logo", notes = PERMISSION
			+ UPDATE_LOGO, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_LOGO })
	public ResponseEntity<TransactionInfo> updateImage(@PathVariable("siteName") String siteName,
			@RequestParam("logo") MultipartFile icon, @RequestParam("feviCon") MultipartFile favIcon)
			throws ParseException {
		try {
			Logo logo = new Logo();
			logo.setSiteName(siteName);
			logo.setLogo(compressBytes(icon.getBytes()));
			logo.setFavicon(compressBytes(icon.getBytes()));
			logoRepository.save(logo);
			return ResponseBuilder.buildOkResponse("sucess");
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseBuilder.buildRecordNotFoundResponse(siteName, resourceBundle.getString("record_not_found"));
		}
	}

	/**
	 * Compress bytes.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

		return outputStream.toByteArray();
	}

	/**
	 * Decompress bytes.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
