package com.example.onlineexamplatform.domain.exam.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamDetailResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.page.PageResponse;
import com.example.onlineexamplatform.domain.exam.service.ExamService;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;
import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "04Exam", description = "사용자(Admin)가 시험 관리(CRUD)하는 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exams")
public class ExamController {

	private final ExamService examService;

	@Operation(summary = "시험 등록 API", description = " Dto로 입력받은 시험과 시험파일Id를 맵핑하여 저장합니다.")
	@CheckAuth(Role.ADMIN)
	@PostMapping
	public ResponseEntity<ApiResponse<ExamResponseDto<ExamFileResponseDto>>> createExam(
		@Valid @RequestBody CreateExamRequestDto requestDto,
		@UserSession SessionUser session) {

		Long userId = session.getUserId();

		ExamResponseDto<ExamFileResponseDto> exam = examService.createExam(requestDto.toCreate(), userId);

		return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM, exam);
	}

	@Operation(summary = "시험 전체 조회 API", description = "등록된 시험 전체를 페이지네이션으로 조회합니다.")
	@CheckAuth(Role.USER)
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<GetExamListResponseDto>>> getExamList(
		@ParameterObject @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<GetExamListResponseDto> examList = examService.getExamList(pageable);

		PageResponse<GetExamListResponseDto> response = new PageResponse<>(examList);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, response);
	}

	@Operation(summary = "시험 검색 조회 API", description = "등록된 시험의 제목을 검색하여 페이지네이션으로 조회합니다.")
	@CheckAuth(Role.USER)
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<PageResponse<GetExamListResponseDto>>> searchExamByTitle(
		@ParameterObject @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
		@Parameter(description = "시험 검색어 입니다.") @RequestParam String examTile) {

		Page<GetExamListResponseDto> examList = examService.searchExamByTitle(pageable, examTile);

		PageResponse<GetExamListResponseDto> response = new PageResponse<>(examList);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, response);
	}

	@Operation(summary = "시험 단건 조회 API", description = "등록된 시험의 ID로 해당 시험을 단건 조회합니다")
	@CheckAuth(Role.ADMIN)
	@GetMapping("/{examId}")
	public ResponseEntity<ApiResponse<ExamResponseDto<ExamFileResponseDto>>> findExamById(
		@Parameter(description = "시험의 ID입니다.") @PathVariable Long examId) {

		ExamResponseDto<ExamFileResponseDto> exam = examService.findExamById(examId);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, exam);
	}

	@Operation(summary = "시험 상세 조회 API", description = "응시자가 응시할 시험 정보와 파일을 조회합니다")
	@CheckAuth(Role.USER)
	@GetMapping("/{examId}/participation")
	public ResponseEntity<ApiResponse<ExamDetailResponseDto>> getExamDetail(
		@Parameter(description = "시험의 ID입니다.") @PathVariable Long examId,
		@UserSession SessionUser session) {

		Long userId = session.getUserId();

		ExamDetailResponseDto examDetail = examService.getExamDetail(userId, examId);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, examDetail);
	}

	@Operation(summary = "시험 수정 API", description = "등록된 시험의 ID로 해당 시험을 찾아 입력된 수정DTO 값을 받아 시험 정보 수정")
	@CheckAuth(Role.ADMIN)
	@PatchMapping("/{examId}")
	public ResponseEntity<ApiResponse<UpdateExamResponseDto>> updateExamById(
		@Parameter(description = "시험의 ID입니다.") @PathVariable Long examId,
		@Valid @RequestBody UpdateExamRequestDto requestDto,
		@UserSession SessionUser session) {

		Long userId = session.getUserId();

		UpdateExamResponseDto exam = examService.updateExamById(userId, examId, requestDto);

		return ApiResponse.onSuccess(SuccessStatus.UPDATE_EXAM, exam);
	}

	@Operation(summary = "시험 삭제 API", description = "등록된 시험의 ID로 해당 시험을 찾아 삭제")
	@CheckAuth(Role.ADMIN)
	@DeleteMapping("/{examId}")
	public ResponseEntity<ApiResponse<Void>> deleteExamById(
		@Parameter(description = "시험의 ID입니다.") @PathVariable Long examId,
		@UserSession SessionUser session) {

		Long userId = session.getUserId();

		examService.deleteExamById(userId, examId);

		return ApiResponse.onSuccess(SuccessStatus.DELETE_EXAM);
	}

}
