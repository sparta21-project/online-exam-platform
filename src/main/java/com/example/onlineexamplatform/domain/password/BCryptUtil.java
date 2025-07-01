package com.example.onlineexamplatform.domain.password;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
	public static String hash(String rawPassword) {
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
	}

	public static boolean matches(String rawPassword, String hashedPassword) {
		return BCrypt.checkpw(rawPassword, hashedPassword);
	}
}
