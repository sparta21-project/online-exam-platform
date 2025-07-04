package com.example.onlineexamplatform.domain.statistics.dto.request.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "문제 조건 검색 요청 DTO")
public record QuestionSearchRequest(

		@Schema(description = "시험 ID", example = "123")
		Long examId,

		@Schema(description = "시험 제목", example = "국어")
		String examTitle,

		@Schema(description = "시험 시작일", example = "2023-01-01")
		LocalDate examStartDate,

		@Schema(description = "시험 종료일", example = "2023-12-31")
		LocalDate examEndDate,

		@Schema(description = "최소 정답률 (%)", example = "20")
		Integer correctRateGte,

		@Schema(description = "최대 정답률 (%)", example = "50")
		Integer correctRateLte

) {

}
