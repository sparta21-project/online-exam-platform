package com.example.onlineexamplatform.domain.statistics.dto.response.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "관리자용 시험 조건 검색 응답 DTO")
public record AdminExamStatisticsSearchResponse(

		@Schema(description = "시험 ID", example = "1")
		Long examId,

		@Schema(description = "공개 여부", example = "true")
		Boolean isPublic,

		@Schema(description = "시험 시작일", example = "2025-06-01")
		LocalDateTime examStartDate,

		@Schema(description = "시험 종료일", example = "2025-06-30")
		LocalDateTime examEndDate,

		@Schema(description = "시험 제목", example = "국어")
		String examTitle,

		@Schema(description = "응시자수 ", example = "30")
		Integer participantCount,

		@Schema(description = "평균 점수", example = "70")
		Integer averageScore
) {

}