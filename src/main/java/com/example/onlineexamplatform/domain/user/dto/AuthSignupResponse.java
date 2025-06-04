package com.example.onlineexamplatform.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthSignupResponse {
	private Long id;
	private String email;
	private String username;
	private String role;
}
