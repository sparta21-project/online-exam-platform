package com.example.onlineexamplatform.domain.userAnswer.entity;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import jakarta.persistence.*;

@Entity
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;

    private String answerText;

    @ManyToOne
    private AnswerSheet answerSheet;
}
