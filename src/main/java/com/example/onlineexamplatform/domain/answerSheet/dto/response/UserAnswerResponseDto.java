package com.example.onlineexamplatform.domain.answerSheet.dto.response;

import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerResponseDto {
    private int questionNumber;
    private String answerText;

    public static UserAnswerResponseDto toUserAnswerResponseDto(UserAnswer userAnswer) {
        return new UserAnswerResponseDto(
                userAnswer.getQuestionNumber(),
                userAnswer.getAnswerText()
        );
    }
}
