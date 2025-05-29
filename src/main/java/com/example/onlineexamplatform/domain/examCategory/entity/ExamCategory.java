package com.example.onlineexamplatform.domain.examCategory.entity;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;

@Entity
public class ExamCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Category categoryType;
}
