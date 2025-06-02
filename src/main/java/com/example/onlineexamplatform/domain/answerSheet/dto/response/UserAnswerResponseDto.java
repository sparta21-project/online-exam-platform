package com.example.onlineexamplatform.domain.answerSheet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerResponseDto {
    private Long questionId;
    private int userAnswer;
}
