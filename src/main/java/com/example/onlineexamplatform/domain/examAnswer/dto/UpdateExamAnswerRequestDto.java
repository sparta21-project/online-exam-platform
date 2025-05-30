package com.example.onlineexamplatform.domain.examAnswer.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateExamAnswerRequestDto {

    @Min(1)
    private Integer questionNumber;

    @Min(1)
    private Integer questionScore;

    private String correctAnswer;
}
