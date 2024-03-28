package com.humaine.admin.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humaine.admin.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserNameAndStatus(String userName, String status);

	User findByUserName(String userName);

}
