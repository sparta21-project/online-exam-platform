package com.example.onlineexamplatform.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthLoginResponse {
	private Long id;
	private String email;
	private String username;
	private String role;
}
