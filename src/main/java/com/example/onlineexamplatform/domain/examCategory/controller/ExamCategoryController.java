package com.example.onlineexamplatform.domain.examCategory.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.examCategory.dto.ExamCategoryCreateRequestDto;
import com.example.onlineexamplatform.domain.examCategory.service.ExamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exam-category")
public class ExamCategoryController {

    private final ExamCategoryService examCategoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@RequestBody @Validated ExamCategoryCreateRequestDto requestDto) {
        examCategoryService.create(requestDto.getExamId(), requestDto.getCategoryId());
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWER_SUCCESS);
    }



}
