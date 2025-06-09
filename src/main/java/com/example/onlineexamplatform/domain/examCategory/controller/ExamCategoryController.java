package com.example.onlineexamplatform.domain.examCategory.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryRequestDto;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryResponseDto;
import com.example.onlineexamplatform.domain.examCategory.service.ExamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam-category")
public class ExamCategoryController {

    private final ExamCategoryService examCategoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody @Validated ExamCategoryRequestDto requestDto) {
        examCategoryService.create(requestDto.getExamCategories());
        return ApiResponse.onSuccess(SuccessStatus.CREATE_EXAM_CATEGORY);
    }

    @GetMapping("/{examId}/exams")
    public ResponseEntity<ApiResponse<List<ExamCategoryResponseDto>>> getByExamId(@PathVariable Long examId) {
        List<ExamCategoryResponseDto> response = examCategoryService.getByExamId(examId);
        return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_CATEGORY, response);
    }

    @GetMapping("/{categoryId}/categories")
    public ResponseEntity<ApiResponse<List<ExamCategoryResponseDto>>> getByCategoryId(@PathVariable Long categoryId) {
        List<ExamCategoryResponseDto> response = examCategoryService.getByCategoryId(categoryId);
        return ApiResponse.onSuccess(SuccessStatus.GET_EXAM_CATEGORY, response);
    }

    @DeleteMapping("/{examCategoryId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long examCategoryId) {
        examCategoryService.delete(examCategoryId);
        return ApiResponse.onSuccess(SuccessStatus.DELETE_EXAM_CATEGORY);
    }
}
