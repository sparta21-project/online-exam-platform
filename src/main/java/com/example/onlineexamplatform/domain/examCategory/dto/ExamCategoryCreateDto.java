package com.example.onlineexamplatform.domain.examCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExamCategoryCreateDto {

    @NotNull(message = "ID는 필수입니다.")
    @Schema(description = "시험 ID", example = "1")
    private Long examId;

    @NotNull(message = "Category 값은 필수입니다.")
    @Schema(description = "권한 ID", example = "1")
    private Long categoryId;
}
