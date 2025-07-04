package com.example.onlineexamplatform.domain.statistics.dto.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminStatisticsVisibilityRequest(

		@Schema(description = "공개 여부 설정 (true = 공개, false = 비공개)", example = "true")
		Boolean isPublic

) {

}
