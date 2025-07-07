package com.example.onlineexamplatform.domain.examCategory.entity;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "exam_category", uniqueConstraints = {@UniqueConstraint(columnNames = {"exam_id", "category_id"})})
@NoArgsConstructor
public class ExamCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public ExamCategory(Exam exam, Category category) {
        this.exam = exam;
        this.category = category;
    }
}
