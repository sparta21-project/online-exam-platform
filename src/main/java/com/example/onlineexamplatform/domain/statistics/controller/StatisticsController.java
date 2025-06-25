package com.example.onlineexamplatform.domain.statistics.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.domain.statistics.dto.ExamStatisticsDto;
import com.example.onlineexamplatform.domain.statistics.service.StatisticsService;
import com.example.onlineexamplatform.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 시험 통계 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "10-Statistics", description = "시험 통계 관련 API")
public class StatisticsController {

	private final StatisticsService statisticsService;
	@CheckAuth(Role.ADMIN)
	@Operation(summary = "시험 통계 조회(관리자 전용)", description = "시험의 평균 점수와 문제별 정답률을 조회합니다.")
	@GetMapping("/exam/{examId}")
	public ResponseEntity<ApiResponse<ExamStatisticsDto>> getExamStatistics(
			@Parameter(description = "시험 ID") @PathVariable Long examId
	) {
		ExamStatisticsDto stats = statisticsService.getExamStatistics(examId);
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_GET_SUCCESS, stats);
	}
}
