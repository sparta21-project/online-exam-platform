package com.example.onlineexamplatform.domain.examCategory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExamCategoryCreateRequestDto {

    @NotNull(message = "ID는 필수입니다.")
    private Long examId;

    @NotNull(message = "Category 값은 필수입니다.")
    private Long categoryId;
}
