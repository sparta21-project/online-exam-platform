package com.example.onlineexamplatform.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ExamStatisticsDto(

		@Schema(description = "시험 평균 점수", example = "82")
		Integer averageScore,

		@Schema(description = "문제별 정답률 리스트")
		List<QuestionCorrectRateDto> questionStats

) {}
