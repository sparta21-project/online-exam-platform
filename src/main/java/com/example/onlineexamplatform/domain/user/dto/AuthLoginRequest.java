package com.example.onlineexamplatform.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequest {
	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
