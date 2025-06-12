package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 요청 정보")
public class AuthSignupRequest {
	@Schema(description = "사용자 이메일", example = "user@example.com")
	@Email
	@NotBlank
	private String email;

	@Schema(description = "비밀번호", example = "P@ssw0rd!")
	@NotBlank
	private String password;

	@Schema(description = "사용자 이름", example = "김민준")
	@NotBlank
	private String username;
}
