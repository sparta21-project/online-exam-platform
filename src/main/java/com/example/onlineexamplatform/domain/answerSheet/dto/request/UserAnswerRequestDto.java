package com.example.onlineexamplatform.domain.answerSheet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerRequestDto {
    private int questionNumber;
    private String answerText;
}
