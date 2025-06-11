package com.example.onlineexamplatform.common.util;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionUserUtil {

	private static final String LOGIN_USER_KEY = "LOGIN_USER_ID";

	private final UserRepository userRepository;

	public Long getCurrentUserId(HttpSession session) {
		Object userId = session.getAttribute(LOGIN_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}
		return (Long) userId;
	}

	public boolean isAdmin(HttpSession session) {
		User user = getUserFromSession(session);
		return user.getRole() == Role.ADMIN;
	}

	public boolean isUser(HttpSession session) {
		User user = getUserFromSession(session);
		return user.getRole() == Role.USER;
	}

	private User getUserFromSession(HttpSession session) {
		Long userId = getCurrentUserId(session);
		return userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
	}
}
