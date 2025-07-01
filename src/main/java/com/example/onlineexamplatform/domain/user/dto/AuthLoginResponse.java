package com.example.onlineexamplatform.domain.user.dto;

import com.example.onlineexamplatform.domain.user.entity.LoginProvider;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 응답 정보")
public class AuthLoginResponse {
	@Schema(description = "사용자 ID", example = "1")
	private Long id;
	@Schema(description = "소셜 로그인 ID", example = "1")
	private Long vendorId;
	@Schema(description = "사용자 이메일", example = "user@example.com")
	private String email;
	@Schema(description = "사용자 이름", example = "김민준")
	private String username;
	@Schema(description = "사용자 권한", example = "USER")
	private Role role;
	@Schema(description = "로그인 제공자", example = "LOCAL")
	private LoginProvider loginProvider;

	public static AuthLoginResponse of(AuthLoginResult result) {
		return new AuthLoginResponse(
			result.getUserId(),
			result.getVendorId(),
			result.getEmail(),
			result.getUsername(),
			result.getRole(),
			result.getLoginProvider()
		);
	}

	public static AuthLoginResponse of(User result) {
		return new AuthLoginResponse(
			result.getId(),
			result.getVendorId(),
			result.getEmail(),
			result.getUsername(),
			result.getRole(),
			result.getLoginProvider()
		);
	}
}
