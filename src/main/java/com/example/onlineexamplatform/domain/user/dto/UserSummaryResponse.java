package com.example.onlineexamplatform.domain.user.dto;

public record UserSummaryResponse(
		Long userId,
		String username,
		String email
) {}

