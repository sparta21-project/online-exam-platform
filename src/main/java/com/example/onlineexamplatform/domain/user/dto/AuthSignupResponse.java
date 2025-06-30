package com.example.onlineexamplatform.domain.user.dto;

import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원가입 응답 정보")
public class AuthSignupResponse {
	@Schema(description = "사용자 ID", example = "1")
	private Long id;

	@Schema(description = "사용자 이메일", example = "user@example.com")
	private String email;

	@Schema(description = "사용자 이름", example = "김민준")
	private String username;

	@Schema(description = "사용자 권한", example = "USER")
	private Role role;

	@Schema(description = "사용자 전화번호", example = "01012345678")
	private String phoneNumber;
}
