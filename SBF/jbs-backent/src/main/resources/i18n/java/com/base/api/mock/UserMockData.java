//package com.base.api.mock;
//
//import com.base.api.entities.User;
//import com.base.api.enums.UserStatus;
//import com.base.api.request.dto.UserSignupDTO;
//
//public class UserMockData {
//
//	public static User user = new User("Pinky Ramnani", "Pinky Ramnani", "pinky.ramnani.user@brainvire,com", "female",
//			null, "9826773833", UserStatus.ACTIVE, 1L, "ROLE_SUPERADMIN");
//
//	public static UserSignupDTO addUser_400() {
//		return new UserSignupDTO(1L, user.getUserProfile().getFirstName(), " ", user.getUserProfile().getLastName(),
//				user.getUserName(), user.getPassword(), user.getUserProfile().getDateOfBirth(),
//				user.getUserProfile().getGender(), user.getUserProfile().getEmail(),
//				user.getUserProfile().getCellPhone(), "", "", user.getUserProfile().getOccupation(),
//				user.getUserProfile().getEmployer());
//	}
//
////	public static UserSignupDTO addUser_200() {
////		return new UserSignupDTO(user.getUserProfile().getFirstName(), user.getUserProfile().getLastName(),
////				user.getUserName(), user.getPassword(), user.getUserProfile().getDateOfBirth(),
////				user.getUserProfile().getGender(), user.getUserProfile().getEmail(),
////				user.getUserProfile().getCellPhone(), user.getUserRole().getRoleName());
////	}
//}