package com.example.onlineexamplatform.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 문제별 정답률 DTO
 */
public record QuestionCorrectRateDto(

		@Schema(description = "문제 번호", example = "1")
		Integer questionNumber,

		@Schema(description = "정답률 (0~100%)", example = "87")
		Integer correctRate

) {}
