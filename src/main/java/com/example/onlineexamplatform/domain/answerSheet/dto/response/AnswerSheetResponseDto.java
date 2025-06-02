package com.example.onlineexamplatform.domain.answerSheet.dto.response;

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
    public static class Create {
        private Long answerSheetId;
        private LocalDateTime createdAt;
    }
    @Getter
    @RequiredArgsConstructor
    public static class Update {
        private Long examId;
        private Long userId;
        private List<UserAnswer> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Submit {
        private Long examId;
        private Long userId;
        private List<UserAnswer> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Get {
        private Long examId;
        private Long userId;
        private List<UserAnswer> answers;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Applicant {
        private String username;
        private String email;
    }
}