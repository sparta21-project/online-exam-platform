package com.example.onlineexamplatform.domain.userAnswer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "시험 문제 번호", example = "1")
    private Integer questionNumber;

    @NotNull
    @Schema(description = "제출할 답안", example = "3")
    private String answerText;

}
