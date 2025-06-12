package com.example.onlineexamplatform.domain.answerSheet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.AnswerSheetRequestDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.AnswerSheetResponseDto;
import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam/{examId}")
public class AnswerSheetController {

	private final AnswerSheetService answerSheetService;

	//빈 답안지 생성 (시험 응시)
	@PostMapping("/answersheet")
	public ResponseEntity<ApiResponse<Void>> createAnswerSheet(
		@PathVariable Long examId,
		HttpServletRequest request
	) {
		Long userId = (Long)request.getSession().getAttribute("userSession");
		answerSheetService.createAnswerSheet(examId, userId);
		return ApiResponse.onSuccess(SuccessStatus.CREATE_ANSWER_SHEET_SUCCESS);
	}

	//답안지 수정 (임시 저장 포함)
	@PatchMapping("/answersheet")
	public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Update>> updateAnswerSheet(
		@PathVariable Long examId,
		@RequestBody AnswerSheetRequestDto.Update requestDto,
		HttpServletRequest request
	) {
		Long userId = (Long)request.getSession().getAttribute("userSession");
		AnswerSheetResponseDto.Update responseDto = answerSheetService.updateAnswerSheet(examId, requestDto, userId);
		return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWERS_SUCCESS, responseDto);
	}

	//답안지 조회
	@GetMapping("/answersheet/{answerSheetId}")
	public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Get>> getAnswerSheet(
		@PathVariable Long examId,
		@PathVariable Long answerSheetId,
		HttpServletRequest request
	) {
		Long userId = (Long)request.getSession().getAttribute("userSession");
		AnswerSheetResponseDto.Get responseDto = answerSheetService.getAnswerSheet(examId, answerSheetId, userId);
		return ApiResponse.onSuccess(SuccessStatus.GET_ANSWERS_SUCCESS, responseDto);
	}

	//    //답안지 삭제
	//    @DeleteMapping("/answersheet/{answerSheetId}")
	//    public ResponseEntity<ApiResponse<Void>> deleteAnswerSheet(
	//            @PathVariable Long examId,
	//            @PathVariable Long answerSheetId,
	//            HttpServletRequest request
	//    ) {
	//        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
	//        answerSheetService.deleteAnswerSheet(examId, answerSheetId, userId);
	//        return ApiResponse.onSuccess(SuccessStatus.DELETE_ANSWER_SHEET_SUCCESS);
	//    }

	//    //답안 최종 제출
	//    @PostMapping("/answersheet/{answerSheetId}")
	//    public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Submit>> submitAnswerSheet(
	//            @PathVariable Long examId,
	//            @PathVariable Long answerSheetId,
	//            @RequestBody AnswerSheetRequestDto.Submit requestDto,
	//            HttpServletRequest request
	//    ) {
	//        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
	//        AnswerSheetResponseDto.Submit responseDto = answerSheetService.submitAnswerSheet(examId, answerSheetId, requestDto, userId);
	//        return ApiResponse.onSuccess(SuccessStatus.SUBMIT_ANSWER_SUCCESS, responseDto);
	//    }

	//    //시험 응시자 조회
	//    @GetMapping("/applicants")
	//    public ResponseEntity<ApiResponse<List<AnswerSheetResponseDto.Applicant>>> getExamApplicants(
	//            @PathVariable Long examId,
	//            HttpServletRequest request
	//    ) {
	//        Long userId = (Long) request.getSession().getAttribute(SESSION_USER_KEY);
	//        List<AnswerSheetResponseDto.Applicant> responseDto = answerSheetService.getExamApplicants(examId, userId);
	//        return ApiResponse.onSuccess(SuccessStatus.GET_APPLICANTS_SUCCESS, responseDto);
	//    }
}