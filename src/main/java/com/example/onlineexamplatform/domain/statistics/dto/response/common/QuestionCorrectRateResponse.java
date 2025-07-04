package com.example.onlineexamplatform.domain.statistics.dto.response.common;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "문제별 정답률 응답 DTO")
public record QuestionCorrectRateResponse(

		@Schema(description = "문제 번호", example = "1")
		Integer questionNumber,

		@Schema(description = "정답률 (0~100%)", example = "87")
		Integer correctRate

) {

}
