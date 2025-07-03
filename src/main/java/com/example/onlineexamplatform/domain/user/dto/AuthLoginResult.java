package com.example.onlineexamplatform.domain.user.dto;

import com.example.onlineexamplatform.domain.user.entity.LoginProvider;
import com.example.onlineexamplatform.domain.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResult {
	private final Long userId;
	private final String vendorId;
	private final String email;
	private final String username;
	private final Role role;
	private final LoginProvider loginProvider;
	private final String sessionId;
}
