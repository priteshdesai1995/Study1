package com.humaine.admin.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.humaine.admin.api.config.AppConfiguration;
import com.humaine.admin.api.util.ErrorMessageUtils;

@SpringBootApplication
public class AdminPortalWebApiApplication {

	@Autowired
	ErrorMessageUtils errorMessageUtils;

	public static void main(String[] args) {
		SpringApplication.run(AdminPortalWebApiApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner masterDataRoles(RoleService repo) {
//		return args -> {
//			List<RolesDTO> roles = new ArrayList<>();
//			roles.add(new RolesDTO("ROLE_SUPERADMIN", "active"));
//			roles.forEach(name -> repo.addRole(name));
//		};
//	}

	@PostConstruct
	public void setMessageCodeSpeperator() {
		AppConfiguration.messageCodeSpeperator = errorMessageUtils.getMessage("error.code.separator");
	}
}
