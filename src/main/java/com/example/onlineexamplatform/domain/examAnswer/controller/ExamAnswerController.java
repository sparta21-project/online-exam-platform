package com.example.onlineexamplatform.domain.examAnswer.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.examAnswer.dto.ExamAnswerResponseDto;
import com.example.onlineexamplatform.domain.examAnswer.dto.SaveExamAnswerRequestDto;
import com.example.onlineexamplatform.domain.examAnswer.service.ExamAnswerService;
import com.example.onlineexamplatform.domain.user.entity.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "ExamAnswer", description = "사용자(Admin)가 작성하는 시험 답안 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/exam-answers")
public class ExamAnswerController {

	private final ExamAnswerService examAnswerService;

	@CheckAuth(Role.USER)
	@Operation(summary = "답안 입력(UPSERT)", description = "Dto로 입력받은 문제 번호와 답안을 저장합니다.")
	@Parameter(description = "시험에 대한 ID입니다.")
	@PostMapping("/{examId}")
	public ResponseEntity<ApiResponse<Void>> saveExamAnswer(@PathVariable Long examId,
		@RequestBody @Valid SaveExamAnswerRequestDto requestDto,
		@UserSession SessionUser sessionUser) {
		examAnswerService.saveExamAnswer(examId, requestDto.getExamAnswers(), sessionUser.getUserId());
		return ApiResponse.onSuccess(SuccessStatus.SAVE_EXAM_ANSWER_SUCCESS);
	}

	@CheckAuth(Role.USER)
	@Operation(summary = "답안 조회(단일)", description = "특정 문항의 답안을 조회합니다")
	@Parameter(description = "해당 문항의 ID 입니다.")
	@GetMapping("/{examAnswerId}")
	public ResponseEntity<ApiResponse<ExamAnswerResponseDto>> getExamAnswer(@PathVariable Long examAnswerId) {
		ExamAnswerResponseDto responseDto = examAnswerService.getExamAnswer(examAnswerId);
		return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_ANSWER_SUCCESS, responseDto);
	}

	@CheckAuth(Role.USER)
	@Operation(summary = "답안 조회(전체)", description = "특정 시험의 답안을 전체 조회합니다")
	@Parameter(description = "해당 시험의 ID 입니다.")
	@GetMapping("/{examId}/exams")
	public ResponseEntity<ApiResponse<Page<ExamAnswerResponseDto>>> getAllExamAnswer(@PathVariable Long examId,
		@PageableDefault Pageable pageable) {
		Page<ExamAnswerResponseDto> responseDtos = examAnswerService.getAllExamAnswer(examId, pageable);
		return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_ANSWER_SUCCESS, responseDtos);
	}

	@CheckAuth(Role.USER)
	@Operation(summary = "답안 삭제", description = "특정 문항의 답안을 삭제합니다")
	@Parameter(description = "해당 문항의 ID 입니다.")
	@DeleteMapping("/{examAnswerId}")
	public ResponseEntity<ApiResponse<Void>> deleteExamAnswer(@UserSession SessionUser sessionUser,
		@PathVariable Long examAnswerId) {
		examAnswerService.deleteExamAnswer(examAnswerId, sessionUser.getUserId());
		return ApiResponse.onSuccess(SuccessStatus.DELETE_EXAM_ANSWER_SUCCESS);
	}
}
