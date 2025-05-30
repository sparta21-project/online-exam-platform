package com.example.onlineexamplatform.domain.exam.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
import com.example.onlineexamplatform.domain.exam.dto.response.GetExamListReponseDto;
import com.example.onlineexamplatform.domain.exam.service.ExamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exams")
public class ExamController {

	private final ExamService examService;

	@PostMapping
	public ResponseEntity<ApiResponse<ExamResponseDto>> createExam(
		@Valid @RequestBody CreateExamRequestDto requestDto, Long userId) {

		ExamResponseDto exam = examService.createExam(requestDto, userId);

		return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM, exam);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<GetExamListReponseDto>>> getExamList() {

		List<GetExamListReponseDto> examList = examService.getExamList();

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, examList);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<GetExamListReponseDto>>> searchExamByTitle(@RequestParam String examTile) {

		List<GetExamListReponseDto> examList = examService.searchExamByTitle(examTile);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM, examList);
	}

	@GetMapping("/{examId}")
	public ResponseEntity<ApiResponse<ExamResponseDto>> findExamById(@PathVariable Long examId) {

		examService.findExamById(examId);

		return ApiResponse.onSuccess(SuccessStatus.FIND_EXAM);
	}

}
