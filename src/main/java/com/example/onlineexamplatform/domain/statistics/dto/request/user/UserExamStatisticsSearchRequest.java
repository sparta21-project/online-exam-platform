package com.example.onlineexamplatform.domain.statistics.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "공개된 시험 조건 검색 요청 DTO")
public record UserExamStatisticsSearchRequest(

		@Schema(description = "시험 시작일", example = "2025-06-01")
		LocalDate examStartDate,

		@Schema(description = "시험 종료일", example = "2025-06-30")
		LocalDate examEndDate,

		@Schema(description = "시험 제목", example = "국어")
		String examTitle,

		@Schema(description = "최소 평균 점수", example = "60")
		Integer scoreGte,

		@Schema(description = "최대 평균 점수", example = "90")
		Integer scoreLte,

		@Schema(description = "최소 응시자 수", example = "10")
		Integer participantCountGte,

		@Schema(description = "최대 응시자 수", example = "100")
		Integer participantCountLte
) {

}
