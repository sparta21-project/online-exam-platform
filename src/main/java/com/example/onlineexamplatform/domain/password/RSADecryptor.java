package com.example.onlineexamplatform.domain.password;

import java.security.PrivateKey;

import javax.crypto.Cipher;

public class RSADecryptor {
	public static String decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes = decryptCipher.doFinal(cipherText);
		return new String(decryptedBytes, "UTF-8");
	}
}
