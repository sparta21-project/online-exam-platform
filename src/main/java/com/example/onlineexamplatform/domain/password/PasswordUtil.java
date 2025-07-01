package com.example.onlineexamplatform.domain.password;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	// 비밀번호 암호화
	public static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	// 비밀번호 비교 (plain vs hash)
	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

	public static boolean verify(@NotBlank String password, String password1) {
		return false;
	}

	public static String hash(String password) {
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("비밀번호는 비어 있을 수 없습니다.");
		}
		if (password.length() < 8 || password.length() > 64) {
			throw new IllegalArgumentException("비밀번호는 8자 이상 64자 이하여야 합니다.");
		}
		if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+]).+$")) {
			throw new IllegalArgumentException("비밀번호는 대문자·소문자·숫자·특수문자를 모두 포함해야 합니다.");
		}
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
}