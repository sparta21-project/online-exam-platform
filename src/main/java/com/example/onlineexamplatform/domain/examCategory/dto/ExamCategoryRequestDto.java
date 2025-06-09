package com.example.onlineexamplatform.domain.examCategory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ExamCategoryRequestDto {
    @NotNull
    @Valid
    private List<ExamCategoryCreateDto> examCategories;
}
