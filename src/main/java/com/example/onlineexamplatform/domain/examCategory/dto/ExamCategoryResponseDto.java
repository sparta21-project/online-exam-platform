package com.example.onlineexamplatform.domain.examCategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExamCategoryResponseDto {

    private Long id;

    private Long examId;

    private String examTitle;

    private Long categoryId;

    private CategoryType categoryType;

    public ExamCategoryResponseDto(ExamCategory examCategory) {
        this.id = examCategory.getId();
        this.examId = examCategory.getExam().getId();
        this.examTitle = examCategory.getExam().getExamTitle();
        this.categoryId = examCategory.getCategory().getId();
        this.categoryType = examCategory.getCategory().getCategoryType();
    }
}
