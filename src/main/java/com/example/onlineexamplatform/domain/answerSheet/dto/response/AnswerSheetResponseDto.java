package com.example.onlineexamplatform.domain.answerSheet.dto.response;

import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "시험 아이디", example = "1")
        private final Long examId;
        @Schema(description = "유저 아이디", example = "2")
        private final Long userId;
        @Schema(description = "답안지 상태", example = "STARTED")
        private final AnswerSheetStatus status;
        @Schema(description = "유저 답안 목록")
        private final List<UserAnswerResponseDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Submit {
        @Schema(description = "시험 아이디", example = "1")
        private final Long examId;
        @Schema(description = "유저 아이디", example = "2")
        private final Long userId;
        @Schema(description = "답안지 상태", example = "STARTED")
        private final AnswerSheetStatus status;
        @Schema(description = "유저 답안 목록")
        private final List<UserAnswerResponseDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Get {
        @Schema(description = "시험 아이디", example = "1")
        private final Long examId;
        @Schema(description = "유저 아이디", example = "2")
        private final Long userId;
        @Schema(description = "답안지 상태", example = "STARTED")
        private final AnswerSheetStatus status;
        @Schema(description = "유저 답안 목록")
        private final List<UserAnswerResponseDto> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Applicant {
        @Schema(description = "유저 이름", example = "testName")
        private final String username;
        @Schema(description = "유저 이메일", example = "test@example.com")
        private final String email;
        @Schema(description = "답안지 상태", example = "SUBMITTED")
        private final AnswerSheetStatus status;
    }
}