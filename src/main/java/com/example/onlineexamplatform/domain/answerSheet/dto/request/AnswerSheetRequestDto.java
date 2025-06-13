package com.example.onlineexamplatform.domain.answerSheet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerSheetRequestDto {
    @Schema(description = "유저 답안 목록")
    private final List<UserAnswerRequestDto> answers;
}
