/**
 * Copyright 2022 Brainvire - All rights reserved.
 */
package com.base.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.api.entities.User;
import com.base.api.enums.UserStatus;

/**
 * This class performs database operations on the user.
 * 
 * @author preyansh_prajapati
 * @author minesh_prajapati
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	@Query("SELECT u FROM User u where u.userName=:userName AND u.status=:status")
	User findByUserNameAndStatus(String userName, UserStatus status);

	@Query("SELECT u FROM User u inner join u.userProfile up WHERE up.email = :email")
	Optional<User> findByEmail(@Param("email") String email);

	User findByUserName(String userName);

	@Query("SELECT u FROM User u  join u.userRole ur WHERE ur.id = ?1")
	List<User> findUserByRoleId(UUID roleId);

	Optional<User> findById(UUID valueOf);

//	@Query("SELECT u FROM User u WHERE u.status = :status")
	List<User> findByStatus(UserStatus status);

	@Query("SELECT u FROM User u inner join u.userProfile up WHERE u.status = :status and up.email like %:email%")
	User findByEmailContaining(@Param("email") String email, @Param("status") String status);
}
