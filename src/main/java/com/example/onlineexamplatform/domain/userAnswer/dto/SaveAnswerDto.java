package com.example.onlineexamplatform.domain.userAnswer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAnswerDto {

    @NotNull
    @Min(1)
    private Integer questionNumber;

    @NotNull
    private String answerText;

}
