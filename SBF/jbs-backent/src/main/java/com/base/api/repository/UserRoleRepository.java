/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.UserRole;

/**
 * This class performs database operations on the role.
 * 
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

	@Query("SELECT r FROM UserRole r where roleName = ?1")
	UserRole findByRoleName(String roleName);

}
