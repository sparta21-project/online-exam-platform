package com.example.onlineexamplatform.domain.examCategory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExamCreateRequestDto {
    @NotNull
    private Long examId;

    @NotNull
    private Long categoryId;
}

