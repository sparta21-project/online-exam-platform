package com.example.onlineexamplatform.domain.answerSheet.controller;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetLockService;
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
import io.swagger.v3.oas.annotations.Operation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(name = "08AnswerSheet", description = "유저의 시험 답안지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exams/{examId}")
public class AnswerSheetController {

	private final AnswerSheetService answerSheetService;
    private final AnswerSheetLockService answerSheetLockService;

    @Operation(summary = "빈 답안지 생성", description = "STARTED 상태의 빈 답안지를 생성합니다.")
    @PostMapping("/answer-sheet")
    public ResponseEntity<ApiResponse<Void>> createAnswerSheet(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        answerSheetLockService.createAnswerSheetWithLock(examId, userId);
        return ApiResponse.onSuccess(SuccessStatus.CREATE_ANSWER_SHEET_SUCCESS);
    }

    //답안지 수정 (임시 저장 포함)
    @Operation(summary = "답안지 수정", description = "유저 답안을 수정/삭제하고 IN_PROGRESS 상태로 변경합니다.")
    @PatchMapping("/answer-sheet")
    public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Update>> updateAnswerSheet(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            @RequestBody AnswerSheetRequestDto requestDto,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        AnswerSheetResponseDto.Update responseDto = answerSheetService.updateAnswerSheet(examId, requestDto, userId);
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWERS_SUCCESS, responseDto);
    }

    //답안지 조회
    @Operation(summary = "답안지 단건 조회", description = "유저 답안지를 조회합니다.")
    @GetMapping("/answer-sheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Get>> getAnswerSheet(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            @Parameter(description = "답안지에 대한 ID입니다.")
            @PathVariable Long answerSheetId,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        AnswerSheetResponseDto.Get responseDto = answerSheetService.getAnswerSheet(examId, answerSheetId, userId);
        return ApiResponse.onSuccess(SuccessStatus.GET_ANSWERS_SUCCESS, responseDto);
    }

    //답안지 삭제
    @Operation(summary = "답안지 삭제", description = "유저 답안지를 삭제합니다.")
    @DeleteMapping("/answer-sheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<Void>> deleteAnswerSheet(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            @Parameter(description = "답안지에 대한 ID입니다.")
            @PathVariable Long answerSheetId,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        answerSheetService.deleteAnswerSheet(examId, answerSheetId, userId);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_ANSWER_SHEET_SUCCESS);
    }

    //답안 최종 제출
    @Operation(summary = "답안지 최종 제출", description = "유저 답안지를 최종 제출합니다.")
    @PostMapping("/answer-sheet/{answerSheetId}")
    public ResponseEntity<ApiResponse<AnswerSheetResponseDto.Submit>> submitAnswerSheet(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            @Parameter(description = "답안지에 대한 ID입니다.")
            @PathVariable Long answerSheetId,
            @RequestBody AnswerSheetRequestDto requestDto,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        AnswerSheetResponseDto.Submit responseDto = answerSheetService.submitAnswerSheet(examId, answerSheetId, requestDto, userId);
        return ApiResponse.onSuccess(SuccessStatus.SUBMIT_ANSWER_SUCCESS, responseDto);
    }

    //시험 응시자 조회
    @Operation(summary = "시험 응시자 조회", description = "시험의 응시자들을 조회합니다.")
    @GetMapping("/applicants")
    public ResponseEntity<ApiResponse<List<AnswerSheetResponseDto.Applicant>>> getExamApplicants(
            @Parameter(description = "시험에 대한 ID입니다.")
            @PathVariable Long examId,
            HttpServletRequest request
    ) {
        UserSession userSession = (UserSession) request.getAttribute("userSession");
        if (userSession == null) {
            throw new ApiException(ErrorStatus.USER_SESSION_NOT_FOUND);
        }
        Long userId = userSession.getUserId();
        List<AnswerSheetResponseDto.Applicant> responseDto = answerSheetService.getExamApplicants(examId, userId);
        return ApiResponse.onSuccess(SuccessStatus.GET_APPLICANTS_SUCCESS, responseDto);
    }
}