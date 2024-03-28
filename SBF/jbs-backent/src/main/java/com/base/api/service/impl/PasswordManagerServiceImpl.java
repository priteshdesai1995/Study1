package com.base.api.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.api.repository.PasswordManagerRepository;
import com.base.api.service.PasswordManagerService;

@Service(value = "PasswordManagerService")
public class PasswordManagerServiceImpl implements PasswordManagerService {
	@Autowired
	PasswordManagerRepository passwordManagerRepository;

	@Override
	public boolean comparePassword(UUID userId, String password) {
		return passwordManagerRepository.comparePassword(userId, password);
	}

	@Override
	public boolean changePassword(UUID userId, String newpassword) {
		return passwordManagerRepository.changePassword(userId, newpassword);
	}

}
