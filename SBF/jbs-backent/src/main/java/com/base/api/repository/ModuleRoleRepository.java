package com.base.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.ModuleRoleEntity;
import com.base.api.entities.UserRole;

@Repository
public interface ModuleRoleRepository extends JpaRepository<ModuleRoleEntity, Long> {

	List<ModuleRoleEntity> findByRole(UserRole role);
}
