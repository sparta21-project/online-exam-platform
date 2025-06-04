package com.example.onlineexamplatform.domain.userAnswer.entity;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;

    private String answerText;

    @ManyToOne
    private AnswerSheet answerSheet;

    public UserAnswer(AnswerSheet answerSheet, int questionNumber, String answerText) {
        this.answerSheet = answerSheet;
        this.questionNumber = questionNumber;
        this.answerText = answerText;
    }

    public void updateAnswer(String answerText) {
        this.answerText = answerText;
    }

}
