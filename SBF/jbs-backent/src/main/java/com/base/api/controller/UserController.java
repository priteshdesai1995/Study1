package com.base.api.controller;

import static com.base.api.constants.Constants.EMAIL;
import static com.base.api.constants.Constants.TOKEN;
import static com.base.api.constants.PermissionConstants.ADD_USER;
import static com.base.api.constants.PermissionConstants.PERMISSION;
import static com.base.api.constants.PermissionConstants.USER_CHANGE_PASSWORD;
import static com.base.api.constants.PermissionConstants.USER_CHECK_PASSWORD_TOKEN_VELIDITY;
import static com.base.api.constants.PermissionConstants.USER_FORGOT_PASSWORD;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.api.annotations.SecuredWIthPermission;
import com.base.api.constants.Constants;
import com.base.api.constants.RoleConstants;
import com.base.api.entities.PasswordResetToken;
import com.base.api.entities.User;
import com.base.api.exception.PasswordException;
import com.base.api.exception.TokenNotFoundException;
import com.base.api.exception.UserNotFoundException;
import com.base.api.repository.PasswordResetTokenRepository;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.ResetPassword;
import com.base.api.request.dto.UserSignupDTO;
import com.base.api.service.UserService;
import com.base.api.utils.ResponseBuilder;
import com.base.api.utils.TransactionInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * @author preyansh_prajapati
 * 
 *         This is the controller for the user APIs There is no need of the
 *         controller to be serialize
 * 
 */
@RequestMapping("/user")
@Api(tags = "User Management API", description = "Rest APIs for the user management")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ResourceBundle resourceBundle;

    /**
     * Test.
     *
     * @return the string
     */
    @GetMapping("/test")
    @ApiOperation(value = "testing")
    public String test() {
        log.info("This is test");
        return "test is working";
    }

    /**
     * @param userSignupDTO
     * @return
     * 
     *         This method will execute when the user hit the API for sign up.
     * @throws Exception
     */
    @PostMapping(value = { "/add-user", "/signup" })
    @ApiOperation(value = "API Endpoint to add the user", notes = PERMISSION + ADD_USER, authorizations = {
            @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
    @SecuredWIthPermission(permissions = { ADD_USER })
    public ResponseEntity<TransactionInfo> addUser(@Valid @RequestBody UserSignupDTO userSignupDTO) throws Exception {
        log.info("API hit for the user sign up");
        User user = userService.addUser(userSignupDTO, RoleConstants.ROLE_SUPER_ADMIN);
        if (user != null) {
            return ResponseBuilder.buildCRUDResponse(user, resourceBundle.getString("user_add"), HttpStatus.CREATED);
        }

        log.info("User signup successfull");
        return ResponseBuilder.buildRecordNotFoundResponse(user, resourceBundle.getString("user_add_fail"));
    }

    /**
     * @param userEmail
     * @return
     * 
     *         This method will execute when user will hit the API of forgot
     *         password.
     * 
     * @throws Exception
     */
    @PostMapping(value = "/forgotpassword")
    @ApiOperation(value = "Forgot password API endpoint")
    public ResponseEntity<TransactionInfo> forgotPassword(@RequestParam(EMAIL) String userEmail) throws Exception {

        log.info("API hit of the forgot password" + userEmail);
        boolean isEmailSend = userService.sendEmailForForgotPassword(userEmail);
        if (isEmailSend) {
            log.info("New password is send to email {} ", userEmail);
            return ResponseBuilder.buildCRUDResponse(isEmailSend,
                    resourceBundle.getString("user_password_send_to_email"), HttpStatus.OK);
        }
        return ResponseBuilder.buildMessageCodeResponse(resourceBundle.getString("user_password_send_to_email_issue"),
                "406", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * @param resetPassword
     * @param token
     * @return
     * 
     *         This method will execute when user will hit the API of change
     *         password.
     * @throws Exception
     * 
     */
    @PutMapping(value = "/reset-password")
    @ApiOperation(value = "Change Password Api Endpoint", notes = PERMISSION + USER_CHANGE_PASSWORD)
    public ResponseEntity<TransactionInfo> changePassword(@Valid @NotNull @RequestBody ResetPassword resetPassword,
            @RequestParam(TOKEN) String token) throws Exception {

        log.info("API hit for change the password");
        boolean isTokenExpired = userService.findTokenIsExpired(token);
        boolean isTokenValid = userService.validatePasswordResetToken(token);

        String newPassword = resetPassword.getPassword();
        String confirmPassword = resetPassword.getConfirmPassword();

  
        if (isTokenExpired || !isTokenValid) {
            log.error("The token : " + token + " is expried or invalid");
            throw new TokenNotFoundException("token.not.valid");
        }

        if (!newPassword.equals(confirmPassword)) {
            log.error("password and confirm password are not same");
            throw new PasswordException("password.confirm.password.not.same");
        }
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        UUID userId = passwordResetToken.getUser().getId();
        Optional<User> userOption = userRepository.findById(userId);
        User user = userOption.get();

        if (user == null) {
            log.error("Not able to fetch user from token " + token);
            throw new UserNotFoundException("not.able.to.fetch.user.from.given.token");
        }
        

        user.setPassword(passwordEncoder.encode(confirmPassword));
        userRepository.save(user);
       
        passwordResetTokenRepository.save(passwordResetToken);
        log.info("The new password is set successfully");
//		TODO : Call a method that will send email 
//		userService.registerOrChangePasswordEmail(user, Constants.CHANGE_PASSWORD);

        userService.changePassword(resetPassword, token);

        log.info("UserController : change password successfully ");

        return ResponseBuilder.buildCRUDResponse("success", resourceBundle.getString("update_password"), HttpStatus.OK);
    }

    /**
     * @param token
     * @return
     * 
     *         This API is used to check if reset token is valid or not
     */
    @PostMapping(value = "/password/validate-reset-token")
    @ApiOperation(value = "Method to check if reset password token is valid or not", notes = PERMISSION
            + USER_CHECK_PASSWORD_TOKEN_VELIDITY, authorizations = {
                    @Authorization(value = Constants.AUTHORIZATION_HEADER_KEY) })
//	@SecuredWIthPermission(permissions = { USER_CHECK_PASSWORD_TOKEN_VELIDITY })
    public ResponseEntity<TransactionInfo> checkTokenValidity(@RequestParam(TOKEN) String token) {

        log.info("API hit to ckech the token validity");
        boolean isTokenExpired = userService.findTokenIsExpired(token);

        if (isTokenExpired) {
            log.error("Token is expired ");
            throw new TokenNotFoundException("token.expired");
        }
        log.info("reset password token is valid");
        return ResponseBuilder.buildCRUDResponse("success", resourceBundle.getString("reset_password_token_is_valid"),
                HttpStatus.OK);
    }
}
