package com.example.onlineexamplatform.domain.statistics.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.domain.statistics.dto.request.admin.AdminExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.admin.AdminStatisticsVisibilityRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.common.QuestionSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateSearchResponse;
import com.example.onlineexamplatform.domain.statistics.service.ExamStatisticsService;
import com.example.onlineexamplatform.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자용 시험 통계 API 컨트롤러 - 단건 조회 - 조건 기반 목록 조회 - 공개 여부 수정 - 문제 조건 조회
 */
@Tag(name = "10 - Admin Statistics", description = "관리자용 시험 통계 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {

	private final ExamStatisticsService statisticsService;

	@CheckAuth(Role.ADMIN)
	@GetMapping("/{examId}")
	@Operation(summary = "시험 통계 단건 조회 ", description = "특정 시험의 통계 정보를 관리자 권한으로 조회합니다.")
	public ResponseEntity<ApiResponse<AdminExamStatisticsResponse>> getExamStatistics(
			@PathVariable Long examId) {
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_GET_SUCCESS,
				statisticsService.getExamStatistics(examId));
	}

	@CheckAuth(Role.ADMIN)
	@PatchMapping("/{examId}/visibility")
	@Operation(summary = "시험 통계 공개 여부 설정 ", description = "시험 통계의 공개 여부를 변경합니다.")
	public ResponseEntity<ApiResponse<String>> updateVisibility(
			@PathVariable Long examId,
			@RequestBody AdminStatisticsVisibilityRequest request
	) {
		String message = statisticsService.updateVisibility(examId, request.isPublic());
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_VISIBILITY_UPDATED, message);
	}

	@CheckAuth(Role.ADMIN)
	@Operation(summary = "문제 조건 검색", description = "조건이 없으면 전체, 조건이 있으면 필터링(기간, 제목, 정답률)하여 문제 목록을 조회합니다.")
	@GetMapping("/questions")
	public ResponseEntity<ApiResponse<List<QuestionCorrectRateSearchResponse>>> searchQuestionsByCondition(
			@ParameterObject QuestionSearchRequest condition) {
		List<QuestionCorrectRateSearchResponse> result = statisticsService.searchQuestionsByCondition(
				condition, true);
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_SEARCH_SUCCESS, result);
	}

	@GetMapping
	@CheckAuth(Role.ADMIN)
	@Operation(summary = "시험 조건 검색", description = "조건이 없으면 전체, 조건이 있으면 필터링(기간, 제목, 평균 점수, 응시자수, 공개 여부)된 시험 통계 목록을 조회합니다.")
	public ResponseEntity<ApiResponse<List<AdminExamStatisticsSearchResponse>>> searchStatisticsByAdmin(
			@ParameterObject AdminExamStatisticsSearchRequest condition) {
		List<AdminExamStatisticsSearchResponse> result =
				statisticsService.searchAdminExamStatisticsByCondition(condition);
		return ApiResponse.onSuccess(SuccessStatus.STATISTICS_SEARCH_SUCCESS, result);
	}
}
