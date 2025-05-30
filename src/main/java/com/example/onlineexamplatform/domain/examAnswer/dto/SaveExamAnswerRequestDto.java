package com.example.onlineexamplatform.domain.examAnswer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SaveExamAnswerRequestDto {

    @NotNull
    @Min(1)
    private Integer questionNumber;

    @NotNull
    @Min(1)
    private Integer questionScore;

    @NotBlank
    private String correctAnswer;
}
