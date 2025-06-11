package com.example.onlineexamplatform.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserProfileResponse {
	private Long id;
	private String email;
	private String username;
	private String role;
}
