package com.example.onlineexamplatform.domain.examAnswer.entity;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(nullable = false)
    private Integer questionNumber;

    @Column(nullable = false)
    private Integer questionScore;

    @Column(nullable = false)
    private String correctAnswer;

    public ExamAnswer(Exam exam, Integer questionNumber, Integer questionScore, String correctAnswer) {
        this.exam = exam;
        this.questionNumber = questionNumber;
        this.questionScore = questionScore;
        this.correctAnswer = correctAnswer;
    }

    public void updateQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void updateQuestionScore(Integer questionScore) {
        this.questionScore = questionScore;
    }

    public void updateCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
