package com.humaine.portal.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.portal.api.model.UserRole;

@Repository
public interface RoleAdminRepository extends JpaRepository<UserRole, Long> {

}
