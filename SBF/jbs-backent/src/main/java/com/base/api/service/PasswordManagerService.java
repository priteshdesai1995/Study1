package com.base.api.service;

import java.util.UUID;

public interface PasswordManagerService {
	public boolean comparePassword(UUID userId, String password);

	public boolean changePassword(UUID userId, String newpassword);
}
