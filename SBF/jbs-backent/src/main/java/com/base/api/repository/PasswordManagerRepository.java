package com.base.api.repository;

import java.util.UUID;

public interface PasswordManagerRepository {
	public boolean comparePassword(UUID userId, String password);

	public boolean changePassword(UUID userId, String newpassword);
}
