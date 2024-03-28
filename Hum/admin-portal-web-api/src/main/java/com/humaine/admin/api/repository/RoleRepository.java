package com.humaine.admin.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.admin.api.model.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {

}
