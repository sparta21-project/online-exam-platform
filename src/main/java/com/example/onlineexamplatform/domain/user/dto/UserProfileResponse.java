package com.example.onlineexamplatform.domain.user.dto;

import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "프로필 조회 응답 정보")
public class UserProfileResponse {

	@Schema(description = "사용자 ID", example = "1")
	private Long id;

	@Schema(description = "사용자 이름", example = "김민준")
	private String email;

	@Schema(description = "사용자 이름", example = "김민준")
	private String username;

	@Schema(description = "사용자 권한", example = "USER")
	private Role role;
}
