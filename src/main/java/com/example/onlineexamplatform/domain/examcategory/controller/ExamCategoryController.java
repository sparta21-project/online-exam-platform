package com.example.onlineexamplatform.domain.examcategory.controller;

import com.example.onlineexamplatform.domain.examcategory.dto.ExamCreateRequestDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamServiceResponseDto;
import com.example.onlineexamplatform.domain.examcategory.dto.ExamUpdateRequestDto;
import com.example.onlineexamplatform.domain.examcategory.service.ExamCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam")
public class ExamCategoryController {
    private final ExamCategoryService examCategoryService;

    //  카테고리 생성
    @PostMapping("/exam-category")
    public ResponseEntity<ExamServiceResponseDto> create(@RequestBody @Validated ExamCreateRequestDto requestDto) {
        ExamServiceResponseDto response = examCategoryService.create(requestDto);
        return ResponseEntity.ok(response);
    }

    //  특정 시험(examId)의 카테고리 조회
    @GetMapping("/{examId}/examcategory/{examcategoryId}")
    public ResponseEntity<List<ExamServiceResponseDto>> getByExamId(
            @PathVariable Long examId,
            @PathVariable Long examcategoryId
    ) {
        List<ExamServiceResponseDto> categories = examCategoryService.findByExamIdAndCategoryId(examId, examcategoryId);
        return ResponseEntity.ok(categories);
    }

    //  카테고리 수정
    @PutMapping("/exam-category/{examCategoryId}")
    public ResponseEntity<ExamServiceResponseDto> update(
            @PathVariable Long examcategoryId,
            @RequestBody @Validated ExamUpdateRequestDto requestDto) {
        ExamServiceResponseDto response = examCategoryService.update(examcategoryId, requestDto);
        return ResponseEntity.ok(response);
    }

    //  카테고리 삭제
    @DeleteMapping("/exam-category/{examCategoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long examCategoryId) {
        examCategoryService.delete(examCategoryId);
        return ResponseEntity.noContent().build();
    }
}

