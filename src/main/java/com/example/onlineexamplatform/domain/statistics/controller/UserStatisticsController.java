package com.example.onlineexamplatform.domain.statistics.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.statistics.dto.request.common.QuestionSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.user.UserExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.service.ExamStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자용 공개 시험 통계 API 컨트롤러 - 단건 조회 - 조건 검색 - 문제 조건 검색 (공개 시험만)
 */
@Tag(name = "11 - Public Statistics", description = "공개된 시험 통계 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/statistics")
public class UserStatisticsController {

	private final ExamStatisticsService statisticsService;

	@GetMapping("/{examId}")
	@Operation(summary = "공개된 시험 통계 조회", description = "공개된 특정 시험의 통계정보를 단건 조회합니다.")
	public ResponseEntity<ApiResponse<UserExamStatisticsResponse>> getUserExamStatistics(
			@PathVariable Long examId) {
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_GET_SUCCESS,
				statisticsService.getUserExamStatistics(examId));
	}

	@Operation(summary = "공개된 시험의 문제 조건 검색", description = "조건이 없으면 전체, 조건이 있으면 필터링(기간, 제목, 정답률)하여 공개된 시험의 문제 목록을 조회합니다.")
	@GetMapping("/questions")
	public ResponseEntity<ApiResponse<List<QuestionCorrectRateSearchResponse>>> searchQuestionsByCondition(
			@ParameterObject QuestionSearchRequest condition) {
		List<QuestionCorrectRateSearchResponse> result = statisticsService.searchQuestionsByCondition(
				condition, false);
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_SEARCH_SUCCESS, result);
	}

	@Operation(summary = "공개된 시험 통계 조건 검색", description = "조건이 없으면 전체, 조건이 있으면 필터링(기간, 제목, 평균 점수, 응시자수)하여  공개된 시험 통계 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserExamStatisticsSearchResponse>>> searchUserExamStatisticsByCondition(
			@ParameterObject UserExamStatisticsSearchRequest condition) {
		List<UserExamStatisticsSearchResponse> result = statisticsService.searchUserExamStatisticsByCondition(
				condition);
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_SEARCH_SUCCESS, result);
	}
}
