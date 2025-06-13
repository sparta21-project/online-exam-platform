package com.example.onlineexamplatform.domain.answerSheet.dto.response;

import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerResponseDto {

    @NotNull
    @Min(1)
    @Schema(description = "문항 번호", example = "1")
    private int questionNumber;

    @NotNull
    @Min(1)
    @Schema(description = "사용자 답안", example = "2")
    private String answerText;

    public static UserAnswerResponseDto toUserAnswerResponseDto(UserAnswer userAnswer) {
        return new UserAnswerResponseDto(
                userAnswer.getQuestionNumber(),
                userAnswer.getAnswerText()
        );
    }
}
