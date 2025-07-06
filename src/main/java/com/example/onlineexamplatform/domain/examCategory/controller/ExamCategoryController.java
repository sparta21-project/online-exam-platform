package com.example.onlineexamplatform.domain.examCategory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryRequestDto;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryResponseDto;
import com.example.onlineexamplatform.domain.examCategory.service.ExamCategoryService;
import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "06ExamCategory", description = "사용자(Admin)가 생성하는 시험 권한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exam-category")

public class ExamCategoryController {

	private final ExamCategoryService examCategoryService;

	@CheckAuth(Role.ADMIN)
	@Operation(summary = "권한 등록", description = "Dto로 입력받은 시험과 권한을 저장합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> create(@RequestBody @Validated ExamCategoryRequestDto requestDto) {
		examCategoryService.create(requestDto.getExamCategories());
		return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM_CATEGORY);
	}

	@CheckAuth(Role.ADMIN)
	@Operation(summary = "권한 조회 By Exam", description = "시험을 볼수 있는 권한들의 목록을 조회합니다.")
	@Parameter(description = "시험의 ID입니다.")
	@GetMapping("/{examId}/exams")
	public ResponseEntity<ApiResponse<List<ExamCategoryResponseDto>>> getByExamId(@PathVariable Long examId) {
		List<ExamCategoryResponseDto> response = examCategoryService.getByExamId(examId);
		return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_CATEGORY, response);
	}

	@CheckAuth(Role.USER)
	@Operation(summary = "시험 조회 By Category", description = "해당 권한으로 응시 가능한 시험의 목록을 조회합니다.")
	@Parameter(description = "권한의 ID입니다.")
	@GetMapping("/{categoryId}/categories")
	public ResponseEntity<ApiResponse<List<ExamCategoryResponseDto>>> getByCategoryId(@PathVariable Long categoryId) {
		List<ExamCategoryResponseDto> response = examCategoryService.getByCategoryId(categoryId);
		return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_CATEGORY, response);
	}

	@CheckAuth(Role.ADMIN)
	@Operation(summary = "시험 권한 삭제", description = "시험에 등록되어 있던 응시 가능 권한을 삭제합니다.")
	@Parameter(description = "시험 권한의 ID입니다.")
	@DeleteMapping("/{examCategoryId}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long examCategoryId) {
		examCategoryService.delete(examCategoryId);
		return ApiResponse.onSuccess(SuccessStatus.DELETE_EXAM_CATEGORY);
	}
}
