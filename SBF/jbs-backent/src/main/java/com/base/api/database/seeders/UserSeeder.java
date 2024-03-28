/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.database.seeders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.api.annotations.DatabaseSeeder;
import com.base.api.constants.RoleConstants;
import com.base.api.entities.User;
import com.base.api.enums.UserStatus;
import com.base.api.repository.UserRepository;
import com.base.api.request.dto.UserSignupDTO;
import com.base.api.service.UserService;

/**
 * This class save the User to the database.
 * 
 * @author minesh_prajapati
 *
 */
@DatabaseSeeder
@Component("UserSeeder")
public class UserSeeder implements BaseSeeder {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private UserService userService;

	private Set<UserSignupDTO> users = new HashSet<UserSignupDTO>() {
		private static final long serialVersionUID = -3746504529926132702L;
		{
			add(new UserSignupDTO("admin", "admin", "admin", "admin", "Brain@2021", LocalDate.now(), "Male",
					"admin@brainvire.com", "", "", "", "", "", new ArrayList<>()));
		}
	};
	

	@Override
	public void seed() throws Exception {
		for (UserSignupDTO user : users) {
			try {
				userService.addUser(user, RoleConstants.ROLE_SUPER_ADMIN);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			//userService.addUser(user, RoleConstants.ROLE_SUPER_ADMIN);
		}
		
	}

}