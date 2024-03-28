/**
 * Copyright ${year} Brainvire - All rights reserved.
 */
package com.base.api.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.base.api.entities.User;
import com.base.api.filter.UserFilter;
import com.base.api.request.dto.AdminAddUserDTO;
import com.base.api.request.dto.ResetPassword;
import com.base.api.request.dto.UserSignupDTO;
import com.base.api.response.dto.SearchInfo;

/**
 * This interface provides services for Users.
 * 
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
public interface UserService {

	public User findByUserName(String userName);

	User findByUserId(UUID userId);

	public User addUser(UserSignupDTO userSignupDTO, String role) throws Exception;

	public Boolean sendEmailForForgotPassword(String email) throws Exception;

	public Boolean findTokenIsExpired(String token);

	public Boolean validatePasswordResetToken(String token);

	public String registerOrChangePasswordEmail(User user, String changePasswordOrRegister) throws Exception;

	public List<User> findUserByRoleId(UUID roleId);

	public void changePassword(ResetPassword resetPassword, String token);

	String signup(UserSignupDTO userSignupDTO, String role);

	List<User> getUserList(String role);

	String sendEmail(String email);

	String signupByAdmin(AdminAddUserDTO userSignupDTO, String string);

	String saveCSV(MultipartFile file);

	ByteArrayInputStream load(List<User> entities);

	String deleteUser(UUID userId);

	SearchInfo search(UserFilter filter, String role);

	ByteArrayInputStream writeExcel(List<User> userDetails, String filename);

	String updateUserByAdmin(AdminAddUserDTO addUserDTO, UUID userId);

	Boolean findTokenisExpired(String token);

}
