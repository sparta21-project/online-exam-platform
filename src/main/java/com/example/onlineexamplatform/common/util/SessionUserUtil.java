package com.example.onlineexamplatform.common.util;

import com.example.onlineexamplatform.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;

public class SessionUserUtil {

	private static final String LOGIN_USER_KEY = "user";

	public static Long getCurrentUserId(HttpSession session) {
		User user = (User) session.getAttribute(LOGIN_USER_KEY);
		if (user == null) {
			throw new IllegalStateException("세션에 로그인 정보가 없습니다.");
		}
		return user.getId();
	}

	public static boolean isAdmin(HttpSession session) {
		User user = (User) session.getAttribute(LOGIN_USER_KEY);
		if (user == null) {
			throw new IllegalStateException("세션에 로그인 정보가 없습니다.");
		}
		return user.getRole().equals("ADMIN");
	}

	public static boolean isUser(HttpSession session) {
		User user = (User) session.getAttribute(LOGIN_USER_KEY);
		if (user == null) {
			throw new IllegalStateException("세션에 로그인 정보가 없습니다.");
		}
		return user.getRole().equals("USER");
	}
}
