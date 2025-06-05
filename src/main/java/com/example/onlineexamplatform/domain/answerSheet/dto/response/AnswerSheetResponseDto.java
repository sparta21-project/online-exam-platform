package com.example.onlineexamplatform.domain.answerSheet.dto.response;

import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerSheetResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class Update {
        private final Long examId;
        private final Long userId;
        private final AnswerSheetStatus status;
        private final List<UserAnswerResponseDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Submit {
        private final Long examId;
        private final Long userId;
        private final AnswerSheetStatus status;
        private final List<UserAnswerResponseDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Get {
        private final Long examId;
        private final Long userId;
        private final List<UserAnswerResponseDto> answers;
        private final AnswerSheetStatus status;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Applicant {
        private final String username;
        private final String email;
    }
}