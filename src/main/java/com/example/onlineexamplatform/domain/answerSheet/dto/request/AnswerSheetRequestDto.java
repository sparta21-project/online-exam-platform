package com.example.onlineexamplatform.domain.answerSheet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerSheetRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class Update {
        private final List<UserAnswerRequestDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Submit {
        private final List<UserAnswerRequestDto> answers;

    }
}
