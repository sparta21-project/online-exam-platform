package com.example.onlineexamplatform.domain.examAnswer.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.examAnswer.dto.ExamAnswerResponseDto;
import com.example.onlineexamplatform.domain.examAnswer.dto.SaveExamAnswerRequestDto;
import com.example.onlineexamplatform.domain.examAnswer.service.ExamAnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/examAnswers")
public class ExamAnswerController {

    private final ExamAnswerService examAnswerService;

    @PostMapping("/{examId}")
    public ResponseEntity<ApiResponse<Void>> saveExamAnswer(@PathVariable Long examId, @RequestBody @Valid SaveExamAnswerRequestDto requestDto) {
        examAnswerService.saveExamAnswer(examId, requestDto.getExamAnswers());
        return ApiResponse.onSuccess(SuccessStatus.SAVE_EXAM_ANSWER_SUCCESS);
    }

    @GetMapping("/{examAnswerId}")
    public ResponseEntity<ApiResponse<ExamAnswerResponseDto>> getExamAnswer(@PathVariable Long examAnswerId) {
        ExamAnswerResponseDto responseDto = examAnswerService.getExamAnswer(examAnswerId);
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWER_SUCCESS, responseDto);
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ApiResponse<Page<ExamAnswerResponseDto>>> getAllExamAnswer(@PathVariable Long examId, @PageableDefault Pageable pageable) {
        Page<ExamAnswerResponseDto> responseDtos = examAnswerService.getAllExamAnswer(examId, pageable);
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWER_SUCCESS, responseDtos);
    }

    @DeleteMapping("/{examAnswerId}")
    public ResponseEntity<ApiResponse<Void>> deleteExamAnswer(@PathVariable Long examAnswerId) {
        examAnswerService.deleteExamAnswer(examAnswerId);
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWER_SUCCESS);
    }
}
