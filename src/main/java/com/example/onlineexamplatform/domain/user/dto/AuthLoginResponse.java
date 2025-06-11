package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 응답 정보")
public class AuthLoginResponse {
	@Schema(description = "사용자 ID", example = "1")
	private Long id;
	@Schema(description = "사용자 이메일", example = "user@example.com")
	private String email;
	@Schema(description = "사용자 이름", example = "김민준")
	private String username;
	@Schema(description = "사용자 권한", example = "USER")
	private String role;
}
