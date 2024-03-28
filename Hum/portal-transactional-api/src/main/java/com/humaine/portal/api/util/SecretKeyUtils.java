package com.humaine.portal.api.util;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretKeyUtils {

	@Value("${apiKeyGenerator.encryption.key}")
	private String SECRET_KEY = "humaI$HUy2A#D%kd";

	public String generateSecretKey(String email, String username) {
		if (email == null || username == null)
			return null;
		String encodedKey = null;
		String txt = email.trim() + "-" + username + "-" + System.currentTimeMillis();
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(txt.toCharArray(), SECRET_KEY.getBytes(), 65536, 256);
			SecretKey originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			encodedKey = Base64.getEncoder().encodeToString(originalKey.getEncoded());
		} catch (Exception e) {
			encodedKey = null;
		}

		return encodedKey;
	}
}
