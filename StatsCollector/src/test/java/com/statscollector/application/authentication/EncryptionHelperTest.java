package com.statscollector.application.authentication;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class EncryptionHelperTest {
	@Test
	public void testEncryptAndDecryptPassword() throws NoSuchAlgorithmException {
		String testPassword = "testPassword";
		String encryptedPassword = EncryptionHelper.encryptPassword(testPassword);
		String decryptedPassword = EncryptionHelper.decryptPassword(encryptedPassword);
		assertEquals(testPassword, decryptedPassword);
	}
}
