package com.example.onlineexamplatform.domain.examAnswer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SaveExamAnswerDto {

    @NotNull
    @Min(1)
    @Schema(description = "문항 번호", example = "1")
    private Integer questionNumber;

    @NotNull
    @Min(1)
    @Schema(description = "해당 문항 점수", example = "5")
    private Integer questionScore;

    @NotBlank
    @Schema(description = "해당 문항 정답", example = "3")
    private String correctAnswer;
}
