package com.base.api.controller;

import static com.base.api.constants.Constants.AUTHORIZATION_HEADER_KEY;
import static com.base.api.constants.PermissionConstants.CHANGE_STATUS;
import static com.base.api.constants.PermissionConstants.DELETE_USER;
import static com.base.api.constants.PermissionConstants.EXPORT_CSV;
import static com.base.api.constants.PermissionConstants.EXPORT_USER_IN_PDF;
import static com.base.api.constants.PermissionConstants.GET_PROFILE_BY_USERID;
import static com.base.api.constants.PermissionConstants.GET_USER_LIST;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.SAVE_CSV;
import static com.base.api.constants.PermissionConstants.UPDATE_USER;
import static com.base.api.constants.PermissionConstants.UPDATE_USER_PROFILE;
import static com.base.api.constants.PermissionConstants.USER_REPORTS;
import static com.base.api.constants.PermissionConstants.USER_SIGNUP;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.config.GeneratePdfReport;
import com.base.api.entities.User;
import com.base.api.enums.UserStatus;
import com.base.api.filter.UserFilter;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.AdminAddUserDTO;
import com.base.api.request.dto.ProfileDTO;
import com.base.api.request.dto.RolesDto;
import com.base.api.request.dto.UpdateUser;
import com.base.api.request.dto.UpdateUserDto;
import com.base.api.response.dto.SearchInfo;
import com.base.api.security.UserPrincipal;
import com.base.api.service.UserService;
import com.base.api.utils.ErrorStatus;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;
import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.io.IOException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class CommonUserController implements Serializable {

	private static final long serialVersionUID = -3964135686000569295L;

	private static final String ANONYMOUS_USERNAME = "anonymous";

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * User signup.
	 *
	 * @param userSignupDTO the user signup DTO
	 * @return the response entity
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access user sign up", notes = PERMISSION + USER_SIGNUP, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { USER_SIGNUP })
	public ResponseEntity<TransactionInfo> userSignup(@Valid @RequestBody AdminAddUserDTO userSignupDTO) {
		log.info("Request Received for userSignup() ...");
		String result = null;

		List<SimpleGrantedAuthority> auth = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		for (SimpleGrantedAuthority a : auth) {
			switch (a.getAuthority()) {
			case "ROLE_SUPER_ADMIN":
				if (userSignupDTO.getRoleName() != null) {
					result = userService.signupByAdmin(userSignupDTO, userSignupDTO.getRoleName());
				} else {
					result = userService.signupByAdmin(userSignupDTO, "ROLE_USER");
				}
				break;
			case "ROLE_ADMIN":
				if (userSignupDTO.getRoleName() != null) {
					result = userService.signupByAdmin(userSignupDTO, userSignupDTO.getRoleName());
				} else {
					result = userService.signupByAdmin(userSignupDTO, "ROLE_USER");
				}
				break;
			}
		}

		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("user_sign_up"),
					HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("user_sign_up_fail"));
	}

	/**
	 * Admin add user.
	 *
	 * @param userId     the user id
	 * @param addUserDTO the add user DTO
	 * @return the response entity
	 */
	@PutMapping(value = "/update/{userId}", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access user update", notes = PERMISSION + UPDATE_USER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_USER })
	public ResponseEntity<TransactionInfo> adminAddUser(
			@NotNull(message = "UserId Should not be null") @PathVariable("userId") UUID userId,
			@Valid @RequestBody AdminAddUserDTO addUserDTO) {
		log.info("Request Received for adminAddUser() ...");
		String result = userService.updateUserByAdmin(addUserDTO, userId);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

			return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("user_add"), HttpStatus.CREATED);
		}

		return ResponseBuilder.buildInternalServerErrorResponse(result, resourceBundle.getString("user_add_fail"));
	}

	/**
	 * Who A mi.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	// Get profile by userId
	@GetMapping(value = "/{userId}", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access user profile", notes = PERMISSION
			+ GET_PROFILE_BY_USERID, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_PROFILE_BY_USERID })
	public ResponseEntity<TransactionInfo> whoAMi(@PathVariable("userId") UUID userId) {
		User userDetails = userService.findByUserId(userId);

		if (userDetails == null) {

			return ResponseBuilder.buildRecordNotFoundResponse(userDetails,
					resourceBundle.getString("record_not_found"));
		}
		ProfileDTO dto = new ProfileDTO();
		dto.setProfileId(userId);
		dto.setEmail(userDetails.getUserProfile().getEmail());
		dto.setFirstName(userDetails.getUserProfile().getFirstName());
		dto.setLastName(userDetails.getUserProfile().getLastName());
		dto.setUsername(userDetails.getUserName());
		dto.setCellPhone(userDetails.getUserProfile().getCellPhone());
		dto.setDateOfBirth(userDetails.getUserProfile().getDateOfBirth());
		dto.setGender(userDetails.getUserProfile().getGender());
		dto.setStatus(userDetails.getStatus());
		RolesDto roles = new RolesDto();
		roles.setName(userDetails.getUserRole().getRoleName());
		roles.setStatus(userDetails.getUserRole().getStatus());
		dto.setRoles(roles);

		return ResponseBuilder.buildOkResponse(dto);
	}

	/**
	 * Gets the user list.
	 *
	 * @param userFilter the user filter
	 * @return the user list
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/search/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access user list", notes = PERMISSION + GET_USER_LIST, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { GET_USER_LIST })
	public ResponseEntity<TransactionInfo> getUserList(@RequestBody UserFilter userFilter) {

		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		SearchInfo userDetails = new SearchInfo();
		for (SimpleGrantedAuthority autho : authorities) {
			switch (autho.getAuthority()) {
			case "ROLE_SUPER_ADMIN":
				userDetails = userService.search(userFilter, "ROLE_SUPER_ADMIN");
				break;
			case "ROLE_ADMIN":
				userDetails = userService.search(userFilter, "ROLE_ADMIN");
				break;
			}
		}

		if (userDetails == null) {

			return ResponseBuilder.buildRecordNotFoundResponse(userDetails,
					resourceBundle.getString("record_not_found"));
		}

		return ResponseBuilder.buildOkResponse(userDetails);
	}

//	@PutMapping(headers = {
//			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	@Secured({ "ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER" })
//	public ResponseEntity<TransactionInfo> updateSelfProfile(@Valid @NotNull @RequestBody UpdateUserDto updateUserDto) {
//		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//
//		UserEntity userEntity = userService.findByUserId(userPrincipal.getUserId());
//		if (userEntity != null) {
//			userEntity.getUserProfile().setFirstName(updateUserDto.getFirstName());
//			userEntity.getUserProfile().setMiddleName(updateUserDto.getMiddleName());
//			userEntity.getUserProfile().setLastName(updateUserDto.getLastName());
//			userEntity.setUserName(updateUserDto.getUserName());
//			userEntity.getUserProfile().setEmail(updateUserDto.getEmail());
//			userEntity.getUserProfile().setDateOfBirth(updateUserDto.getDateOfBirth());
//			userEntity.getUserProfile().setGender(updateUserDto.getGender());
//			userEntity.getUserProfile().setCellPhone(updateUserDto.getCellPhone());
//			userEntity.getUserProfile().setHomePhone(updateUserDto.getHomePhone());
//			userEntity.getUserProfile().setWorkPhone(updateUserDto.getHomePhone());
//			userEntity.getUserProfile().setOccupation(updateUserDto.getOccupation());
//			userEntity.getUserProfile().setEmployer(updateUserDto.getEmployer());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setAddressLineOne(updateUserDto.getAddress().get(0).getAddressLineOne());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setAddressLineTwo(updateUserDto.getAddress().get(0).getAddressLineTwo());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setAddressType(updateUserDto.getAddress().get(0).getAddressType());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setProvince(updateUserDto.getAddress().get(0).getProvince());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setCity(updateUserDto.getAddress().get(0).getCity());
//			userEntity.getUserProfile().getUserAddresses().iterator().next()
//					.setPostalCode(updateUserDto.getAddress().get(0).getPostalCode());
//			try {
//				userRepository.save(userEntity);
//			} catch (Exception ex) {
//				TransactionInfo transactionInfo = new TransactionInfo(false);
//				transactionInfo.addErrorList("User with same email is already exists");
//				transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
//				return new ResponseEntity<TransactionInfo>(transactionInfo, HttpStatus.BAD_REQUEST);
//			}
//		}
//		TransactionInfo transactionInfo = new TransactionInfo(true);
//		transactionInfo.setStatusCode(HttpStatus.OK.value());
//		return new ResponseEntity<TransactionInfo>(transactionInfo, HttpStatus.OK);
//	}

	/**
	 * Update user profile.
	 *
	 * @param userId        the user id
	 * @param updateUserDto the update user dto
	 * @return the response entity
	 */
	@PutMapping(value = "/{userId}", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access update user profile", notes = PERMISSION
			+ UPDATE_USER_PROFILE, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_USER_PROFILE })
	public ResponseEntity<TransactionInfo> updateUserProfile(
			@NotNull(message = "UserId Should not be null") @PathVariable("userId") UUID userId,
			@Valid @NotNull @RequestBody UpdateUserDto updateUserDto) {

		User userEntity = userService.findByUserId(userId);

		if (userEntity != null) {

			userEntity.getUserProfile().setFirstName(updateUserDto.getFirstName());
			userEntity.getUserProfile().setMiddleName(updateUserDto.getMiddleName());
			userEntity.getUserProfile().setLastName(updateUserDto.getLastName());
			userEntity.setUserName(updateUserDto.getUserName());
			userEntity.getUserProfile().setEmail(updateUserDto.getEmail());
			userEntity.getUserProfile().setDateOfBirth(updateUserDto.getDateOfBirth());
			userEntity.getUserProfile().setGender(updateUserDto.getGender());
			userEntity.getUserProfile().setCellPhone(updateUserDto.getCellPhone());
			userEntity.getUserProfile().setHomePhone(updateUserDto.getHomePhone());
			userEntity.getUserProfile().setWorkPhone(updateUserDto.getHomePhone());
			userEntity.getUserProfile().setOccupation(updateUserDto.getOccupation());
			userEntity.getUserProfile().setEmployer(updateUserDto.getEmployer());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setAddressLineOne(updateUserDto.getAddress().get(0).getAddressLineOne());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setAddressLineTwo(updateUserDto.getAddress().get(0).getAddressLineTwo());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setAddressType(updateUserDto.getAddress().get(0).getAddressType());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setProvince(updateUserDto.getAddress().get(0).getProvince());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setCity(updateUserDto.getAddress().get(0).getCity());
			userEntity.getUserProfile().getUserAddresses().iterator().next()
					.setPostalCode(updateUserDto.getAddress().get(0).getPostalCode());
			try {
				userRepository.save(userEntity);

				return ResponseBuilder.buildCRUDResponse(userEntity, resourceBundle.getString("user_profile_updated"),
						HttpStatus.OK);

			} catch (Exception ex) {
				return ResponseBuilder.buildInternalServerErrorResponse(ex.getMessage(),
						resourceBundle.getString("user_profile_update_fail"));
			}
		}

		return ResponseBuilder.buildRecordNotFoundResponse(userEntity, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Update user.
	 *
	 * @param updateUser the update user
	 * @return the response entity
	 */
	@PutMapping(headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access update user", notes = PERMISSION + UPDATE_USER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { UPDATE_USER })
	public ResponseEntity<TransactionInfo> updateUser(@Valid @NotNull @RequestBody UpdateUser updateUser) {

		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		User userEntity = userService.findByUserId(userPrincipal.getUserId());

		if (userEntity != null) {
			userEntity.getUserProfile().setFirstName(updateUser.getFirstName());
			userEntity.getUserProfile().setLastName(updateUser.getLastName());
			try {
				userRepository.save(userEntity);

				return ResponseBuilder.buildCRUDResponse(userEntity, resourceBundle.getString("user_updated"),
						HttpStatus.OK);
			} catch (Exception ex) {

				return ResponseBuilder.buildInternalServerErrorResponse(ex.getMessage(),
						resourceBundle.getString("user_update_fail"));
			}
		}

		return ResponseBuilder.buildRecordNotFoundResponse(userEntity, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Change status.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	@GetMapping(value = "/changestatus", headers = {
			"Version=V1" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to access User Change Status", notes = PERMISSION
			+ CHANGE_STATUS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_STATUS })
	public ResponseEntity<TransactionInfo> changeStatus(@RequestParam("userId") UUID userId) {
		log.info("Request Received for userSignup() ...");
		User userEntity = userService.findByUserId(userId);
		UserStatus status = userEntity.getStatus();
		if (userEntity != null) {

			userEntity.setStatus(userEntity.getStatus().toString().equalsIgnoreCase("ACTIVE") ? UserStatus.INACTIVE
					: UserStatus.ACTIVE);
			userRepository.save(userEntity);

			return ResponseBuilder.buildStatusChangeResponse(userEntity, resourceBundle.getString("status"));
		}

		return ResponseBuilder.buildRecordNotFoundResponse(userEntity, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Creates the.
	 *
	 * @param file the file
	 * @return the response entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostMapping(value = "/import", consumes = { "multipart/form-data" })
	@ApiOperation(value = "API Endpoint to access import file", notes = PERMISSION + SAVE_CSV, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { SAVE_CSV })
	public ResponseEntity<TransactionInfo> create(@RequestParam("file") MultipartFile file) throws IOException {
		TransactionInfo transactionInfo;
		String result = userService.saveCSV(file);
		if (result.equalsIgnoreCase(HttpStatus.OK.name())) {
			transactionInfo = new TransactionInfo(ErrorStatus.SUCCESS);
			transactionInfo.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<>(transactionInfo, HttpStatus.OK);
		} else {
			transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
			transactionInfo.addErrorList("Could not upload the file: " + file.getOriginalFilename() + "!");
			transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Export CSV.
	 *
	 * @param response the response
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export-users/csv", produces = "text/csv")
	@ApiOperation(value = "API Endpoint to access export users in csv", notes = PERMISSION
			+ EXPORT_CSV, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { EXPORT_CSV })
	public ResponseEntity<InputStreamResource> exportCSV(HttpServletResponse response) throws Exception {
		String filename = "users.csv";

		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		List<User> userDetails = null;
		for (SimpleGrantedAuthority autho : authorities) {
			switch (autho.getAuthority()) {
			case "ROLE_SUPER_ADMIN":
				userDetails = userService.getUserList("ROLE_SUPER_ADMIN");
				break;
			case "ROLE_ADMIN":
				userDetails = userService.getUserList("ROLE_ADMIN");
				break;
			}
		}
		InputStreamResource fileData = new InputStreamResource(userService.load(userDetails));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(fileData);
	}

	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	@DeleteMapping(value = "/delete/{userId}")
	@ApiOperation(value = "API Endpoint to access delete user", notes = PERMISSION + DELETE_USER, authorizations = {
			@Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { DELETE_USER })
	public ResponseEntity<TransactionInfo> deleteUser(@PathVariable("userId") UUID userId) {
		log.info("Request Received for deleteUser() ...");

		String result = null;
		User userEntity = userService.findByUserId(userId);

		if (userEntity != null) {
			result = userService.deleteUser(userId);
			if (result.equalsIgnoreCase(HttpStatus.OK.name())) {

				return ResponseBuilder.buildCRUDResponse(result, resourceBundle.getString("user_deleted"),
						HttpStatus.OK);
			} else {

				return ResponseBuilder.buildInternalServerErrorResponse(result,
						resourceBundle.getString("user_delete_fail"));
			}
		}

		return ResponseBuilder.buildRecordNotFoundResponse(result, resourceBundle.getString("record_not_found"));
	}

	/**
	 * Cities report.
	 *
	 * @return the response entity
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export-users/pdf", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	@ApiOperation(value = "API Endpoint to access export users in pdf", notes = PERMISSION
			+ EXPORT_USER_IN_PDF, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { EXPORT_USER_IN_PDF })
	public ResponseEntity<InputStreamResource> citiesReport() {
		String filename = "users.pdf";
		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		List<User> userDetails = null;
		for (SimpleGrantedAuthority autho : authorities) {
			switch (autho.getAuthority()) {
			case "ROLE_SUPER_ADMIN":
				userDetails = userService.getUserList("ROLE_SUPER_ADMIN");
				break;
			case "ROLE_ADMIN":
				userDetails = userService.getUserList("ROLE_ADMIN");
				break;
			}
		}
		ByteArrayInputStream bis = GeneratePdfReport.usersReport(userDetails);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(bis));
	}

	/**
	 * Users report.
	 *
	 * @param response the response
	 * @return the response entity
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export-users/xlsx", method = RequestMethod.GET)
	@ApiOperation(value = "API Endpoint to access export users in xlsx", notes = PERMISSION
			+ USER_REPORTS, authorizations = { @Authorization(value = AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { USER_REPORTS })
	public ResponseEntity<InputStreamResource> usersReport(HttpServletResponse response) {
		String filename = "users.xlsx";

		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		List<User> userDetails = null;
		for (SimpleGrantedAuthority autho : authorities) {
			switch (autho.getAuthority()) {
			case "ROLE_SUPER_ADMIN":
				userDetails = userService.getUserList("ROLE_SUPER_ADMIN");
				break;
			case "ROLE_ADMIN":
				userDetails = userService.getUserList("ROLE_ADMIN");
				break;
			}
		}
		ByteArrayInputStream stream = userService.writeExcel(userDetails, filename);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
		InputStreamResource fileData = new InputStreamResource(stream);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(fileData);

	}

}
