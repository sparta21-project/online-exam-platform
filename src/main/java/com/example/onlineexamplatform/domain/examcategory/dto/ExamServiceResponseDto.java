package com.example.onlineexamplatform.domain.examcategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import lombok.Getter;

@Getter
public class ExamServiceResponseDto {

    private final Long id;
    private final Long examId;
    private final CategoryType categoryType;

    public ExamServiceResponseDto(Long id, Long examId, CategoryType categoryType) {
        this.id = id;
        this.examId = examId;
        this.categoryType = categoryType;
    }
}
