/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.api.entities.Privilege;

/**
 * This class performs database operations on the permission.
 * 
 * @author minesh_prajapati
 *
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

	@Query("SELECT r.privileges FROM UserRole r where r.id = ?1")
	List<Privilege> findByRoleID(UUID roleId);

}
