package com.example.onlineexamplatform.domain.examAnswer.entity;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;

@Entity
public class ExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int questionNumber;

    private int questionScore;

    private String correctAnswer;

    @ManyToOne
    private Exam exam;
}
