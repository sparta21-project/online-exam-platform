package com.example.onlineexamplatform.domain.answerSheet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerSheetRequestDto {
    private final List<UserAnswerRequestDto> answers;
}
