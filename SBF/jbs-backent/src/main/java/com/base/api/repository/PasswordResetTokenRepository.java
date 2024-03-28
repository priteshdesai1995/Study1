package com.base.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.api.entities.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

	/**
	 * @param token
	 * @return
	 * 
	 * This method will return object of PasswordResetToken from given token
	 * Like return user from userId
	 */
	public PasswordResetToken findByToken(String token);

}
