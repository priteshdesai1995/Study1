package com.example.security.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String name);
}
