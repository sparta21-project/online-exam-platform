package com.example.onlineexamplatform.domain.exam.controller;

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
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.request.UpdateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.UpdateExamResponseDto;
import com.example.onlineexamplatform.domain.exam.page.PageResponse;
import com.example.onlineexamplatform.domain.exam.service.ExamService;
import com.example.onlineexamplatform.domain.examFile.dto.response.ExamFileResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ExamController {

	private final ExamService examService;

	@PostMapping("/admin/{userId}/exams")
	public ResponseEntity<ApiResponse<ExamResponseDto<ExamFileResponseDto>>> createExam(
		@PathVariable Long userId,
		@Valid @RequestBody CreateExamRequestDto requestDto) {

		ExamResponseDto exam = examService.createExam(requestDto.toCreate(), userId);

		return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM, exam);
	}

	@GetMapping("/admin/exams")
	public ResponseEntity<ApiResponse<PageResponse<GetExamListResponseDto>>> getExamList(
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

		// 페이징 적용
		Page<GetExamListResponseDto> examList = examService.getExamList(pageable);

		PageResponse<GetExamListResponseDto> response = new PageResponse<>(examList);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, response);
	}

	@GetMapping("/admin/exams/search")
	public ResponseEntity<ApiResponse<PageResponse<GetExamListResponseDto>>> searchExamByTitle(
		@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam String examTile) {

		Page<GetExamListResponseDto> examList = examService.searchExamByTitle(pageable, examTile);

		PageResponse<GetExamListResponseDto> response = new PageResponse<>(examList);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, response);
	}

	@GetMapping("/admin/exams/{examId}")
	public ResponseEntity<ApiResponse<ExamResponseDto>> findExamById(@PathVariable Long examId) {

		ExamResponseDto exam = examService.findExamById(examId);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, exam);
	}

	@PatchMapping("/admin/exams/{examId}")
	public ResponseEntity<ApiResponse<UpdateExamResponseDto>> updateExamById(
		@PathVariable Long examId,
		@Valid @RequestBody UpdateExamRequestDto requestDto) {

		UpdateExamResponseDto exam = examService.updateExamById(examId, requestDto);

		return ApiResponse.onSuccess(SuccessStatus.UPDATE_EXAM, exam);
	}

	@DeleteMapping("/admin/exams/{examId}")
	public ResponseEntity<ApiResponse<Void>> deleteExamById(@PathVariable Long examId) {

		examService.deleteExamById(examId);

		return ApiResponse.onSuccess(SuccessStatus.DELETE_EXAM);
	}

}
