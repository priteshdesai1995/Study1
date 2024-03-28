package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.UserAdmin;

@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {

	UserAdmin findByUserNameAndStatus(String userName, String status);

	UserAdmin findByUserName(String userName);

}
