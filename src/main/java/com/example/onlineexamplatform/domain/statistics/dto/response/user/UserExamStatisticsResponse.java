package com.example.onlineexamplatform.domain.statistics.dto.response.user;

import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "공개된 시험 단건 조회 응답 DTO")
public record UserExamStatisticsResponse(

		@Schema(description = "시험 시작일", example = "2025-06-01")
		LocalDateTime examStartDate,

		@Schema(description = "시험 종료일", example = "2025-06-30")
		LocalDateTime examEndDate,

		@Schema(description = "시험 제목", example = "국어")
		String examTitle,

		@Schema(description = "응시자수 ", example = "30")
		Integer participantCount,

		@Schema(description = "시험 평균 점수", example = "82")
		Integer averageScore,

		@Schema(description = "문제별 정답률 리스트")
		List<QuestionCorrectRateResponse> questionStats

) {

}
