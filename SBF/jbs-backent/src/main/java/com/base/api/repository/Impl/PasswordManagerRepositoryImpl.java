package com.base.api.repository.Impl;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.base.api.entities.User;
import com.base.api.repository.PasswordManagerRepository;
import com.base.api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository(value = "PasswordManagerRepository")
public class PasswordManagerRepositoryImpl implements PasswordManagerRepository {
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;
	
	/**
	 * Compare password.
	 *
	 * @param userId the user id
	 * @param password the password
	 * @return true, if successful
	 */
	@Override
	public boolean comparePassword(UUID userId, String password) {
		User userEntity = userRepository.getById(userId);				
//		entityManager.createNamedQuery("user.select.by.id", User.class).setParameter("user_id", userId).getSingleResult();

		if (userEntity != null) {
			if (passwordEncoder.matches(password, userEntity.getPassword())) {
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	/**
	 * Change password.
	 *
	 * @param userId the user id
	 * @param newpassword the newpassword
	 * @return true, if successful
	 */
	@Override
	public boolean changePassword(UUID userId, String newpassword) {
		Optional<User> userEntity = userRepository.findById(userId);

		if (userEntity.get() != null) {
			userEntity.get().setPassword(passwordEncoder.encode(newpassword));
			userRepository.save(userEntity.get());
			return true;
		}
		return false;
	}

}
