package com.example.onlineexamplatform.domain.exam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.exam.dto.request.CreateExamRequestDto;
import com.example.onlineexamplatform.domain.exam.dto.response.ExamResponseDto;
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
		@Valid @RequestBody CreateExamRequestDto requestDto) {

		ExamResponseDto exam = examService.createExam(requestDto);

		return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM, exam);
	}

}
