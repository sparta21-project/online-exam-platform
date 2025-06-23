package com.example.onlineexamplatform.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResult {
	private final AuthLoginResponse dto;
	private final String sessionId;

}
