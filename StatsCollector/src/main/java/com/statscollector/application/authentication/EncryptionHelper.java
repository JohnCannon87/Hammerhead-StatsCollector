package com.statscollector.application.authentication;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptionHelper {

	private static final String ENCRYPTION_ALGORITHM = "AES";
	private static final Key ENCRYPTION_KEY = createKey();
	private static final String HASH_ALGORITHM = "SHA-1";

	private static Key createKey() {
		try {
			byte[] key = "Ev932238Wd874gdOpAGiGDI1yx1OV72g".getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance(HASH_ALGORITHM);
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit
			return new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Need UTF-8 Support !");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Need SHA-1 Support !");
		}
	}

	public static String encryptPassword(final String password) {
		String encodedEncryptedValue = null;
		try {
			Cipher cryptoInstance = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cryptoInstance.init(Cipher.ENCRYPT_MODE, ENCRYPTION_KEY);
			byte[] encryptedValue = cryptoInstance.doFinal(password.getBytes("UTF-8"));
			encodedEncryptedValue = new String(new Base64().encode(encryptedValue));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return encodedEncryptedValue;
		}
		return encodedEncryptedValue;
	}

	public static String decryptPassword(final String password) {
		String decryptedValue = null;
		Cipher cryptoInstance;
		try {
			cryptoInstance = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			cryptoInstance.init(Cipher.DECRYPT_MODE, ENCRYPTION_KEY);
			byte[] decodedValue = new Base64().decode(password.getBytes("UTF-8"));
			decryptedValue = new String(cryptoInstance.doFinal(decodedValue));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return decryptedValue;
	}

}
