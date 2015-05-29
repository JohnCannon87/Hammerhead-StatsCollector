package com.statscollector.application.authentication;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Test;

public class EncryptionHelperTest {
	@Test
	public void testEncryptAndDecryptPassword() throws NoSuchAlgorithmException {
		KeyGenerator instance = KeyGenerator.getInstance("AES");
		SecretKey generateKey = instance.generateKey();
		generateKey.getEncoded().toString();
		String testPassword = "testPassword";
		String encryptedPassword = EncryptionHelper.encryptPassword(testPassword);
		String decryptedPassword = EncryptionHelper.decryptPassword(encryptedPassword);
		assertEquals(testPassword, decryptedPassword);
	}
}
