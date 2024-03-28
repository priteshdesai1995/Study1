package com.humaine.portal.api.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class EncryptDecrypt {

	@Value("${password.encryption.ivParameterKey}")
	private String IV_PARAMETER_KEY = "ssdkF$HUy2A#D%kd";

	@Value("${password.encryption.secretKey}")
	private String SECRET_KEY = "weJiSEvR5yAC5ftB";

	private IvParameterSpec ivParameterSpec;
	private SecretKeySpec secretKeySpec;
	private Cipher cipher;

	@PostConstruct
	public void setupKeys() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
		ivParameterSpec = new IvParameterSpec(IV_PARAMETER_KEY.getBytes("UTF-8"));
		secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
		cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	}

	public String encrypt(String toBeEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
		return Base64.encodeBase64String(encrypted);
	}

	public String decrypt(String encrypted) throws InvalidAlgorithmParameterException, InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException {
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
		return new String(decryptedBytes);
	}
}