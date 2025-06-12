package com.example.onlineexamplatform.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 관리자가 특정 응시 권한을 보유한 사용자 목록 조회시 사용되는
 * 사용자 요약 정보를 담는 DTO
 */
@Schema(description = "응시 권한이 부여된 사용자 요약 정보")
public record UserSummaryResponse(

		@Schema(description = "사용자 ID", example = "1")
		Long userId,

		@Schema(description = "사용자 이름", example = "김태형")
		String username,

		@Schema(description = "사용자 이메일", example = "dkdlgusghrm99@gmail.com")
		String email

) {}
