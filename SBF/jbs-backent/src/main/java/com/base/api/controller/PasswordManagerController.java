package com.base.api.controller;

import static com.base.api.constants.Constants.PERMISSION;
import static com.base.api.constants.PermissionConstants.CHANGE_PASSWORD;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.entities.User;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.ChangePasswordDto;
import com.base.api.security.UserPrincipal;
import com.base.api.service.PasswordManagerService;
import com.base.api.service.UserService;
import com.base.api.utils.ErrorStatus;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/password")
@CrossOrigin("*")
public class PasswordManagerController {
	@Autowired
	PasswordManagerService passwordManagerService;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ResourceBundle resourceBundle;

	/**
	 * Change password.
	 *
	 * @param changePasswordDto the change password dto
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@PutMapping(path = "/change", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "API Endpoint to Change Password", notes = PERMISSION + CHANGE_PASSWORD, authorizations = {
			@Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
	@SecuredWIthPermission(permissions = { CHANGE_PASSWORD })
	public ResponseEntity<TransactionInfo> changePassword(
			@Valid @NotNull @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
		TransactionInfo transactionInfo;
		UUID userId;
		if (changePasswordDto.getUserId() != null) {
			userId = changePasswordDto.getUserId();
		} else {
			if (changePasswordDto.getOldPassword() != null) {
				UserPrincipal usrObject = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				userId = usrObject.getUserId();
			} else {
				transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
				transactionInfo.addErrorList(resourceBundle.getString("old_pass_not_empty"));
				transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
			}
		}
		if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
			transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
			transactionInfo.addErrorList(resourceBundle.getString("pass_match"));
			transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
		}

		if (changePasswordDto.getOldPassword() != null) {
			boolean comparePassword = passwordManagerService.comparePassword(userId,
					changePasswordDto.getOldPassword());
			if (!comparePassword) {
				transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
				transactionInfo.addErrorList(resourceBundle.getString("invalid_old_pass"));
				transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);

			}
		}
		if (passwordManagerService.changePassword(userId, changePasswordDto.getNewPassword())) {
			transactionInfo = new TransactionInfo(ErrorStatus.SUCCESS);
			transactionInfo.setStatusCode(HttpStatus.OK.value());
			Optional<User> userEntity = userRepository.findById(userId);
			userService.registerOrChangePasswordEmail(userEntity.get(), "changePassword");
			return new ResponseEntity<>(transactionInfo, HttpStatus.OK);
		} else {
			transactionInfo = new TransactionInfo(ErrorStatus.FAIL);
			transactionInfo.addErrorList(resourceBundle.getString("pass_change_fail"));
			transactionInfo.setStatusCode(HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(transactionInfo, HttpStatus.BAD_REQUEST);
		}
	}
}
