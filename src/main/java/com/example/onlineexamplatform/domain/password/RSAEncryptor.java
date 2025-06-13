package com.example.onlineexamplatform.domain.password;

import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSAEncryptor {
	public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return encryptCipher.doFinal(plainText.getBytes("UTF-8"));
	}
}
