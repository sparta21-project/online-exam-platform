package com.example.onlineexamplatform.domain.examAnswer.entity;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;

@Entity
public class ExamAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int questionNumber;

    @Column(nullable = false)
    private int questionScore;

    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
