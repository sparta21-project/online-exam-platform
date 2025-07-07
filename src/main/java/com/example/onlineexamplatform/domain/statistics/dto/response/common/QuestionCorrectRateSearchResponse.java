package com.example.onlineexamplatform.domain.statistics.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "문제 조건 검색 응답 DTO")
public record QuestionCorrectRateSearchResponse(

		@Schema(description = "시험 ID", example = "1")
		Long examId,

		@Schema(description = "시험 시작일", example = "2025-06-01")
		LocalDateTime examStartDate,

		@Schema(description = "시험 종료일", example = "2025-06-30")
		LocalDateTime examEndDate,

		@Schema(description = "시험 제목", example = "국어")
		String examTitle,

		@Schema(description = "문제 번호", example = "5")
		Integer questionNumber,

		@Schema(description = "정답률 (%)", example = "27")
		Integer correctRate

) {

}
