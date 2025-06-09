package com.example.onlineexamplatform.domain.examAnswer.dto;

import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExamAnswerResponseDto {

    private Long examId;

    private Integer questionNumber;

    private Integer questionScore;

    private String correctAnswer;

    public ExamAnswerResponseDto(ExamAnswer examAnswer) {
        this.examId = examAnswer.getExam().getId();
        this.questionNumber = examAnswer.getQuestionNumber();
        this.questionScore = examAnswer.getQuestionScore();
        this.correctAnswer = examAnswer.getCorrectAnswer();
    }
}
