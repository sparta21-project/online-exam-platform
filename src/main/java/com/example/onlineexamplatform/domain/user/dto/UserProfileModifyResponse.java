package com.example.onlineexamplatform.domain.user.dto;

import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "프로필 수정 응답 정보")
public class UserProfileModifyResponse {

	@Schema(description = "사용자 ID", example = "1")
	private Long id;

	@Schema(description = "사용자 이메일", example = "updated@example.com")
	private String email;

	@Schema(description = "사용자 이름", example = "새이름")
	private String username;

	@Schema(description = "사용자 권한", example = "USER")
	private Role role;

	@Schema(description = "사용자 전화번호", example = "01012345678")
	private String phoneNumber;
}
