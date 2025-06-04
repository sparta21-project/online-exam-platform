package com.example.onlineexamplatform.domain.examcategory.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ExamCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long examId;

    @ManyToOne
    private Category category;

    public ExamCategory(Long examId, Category category) {
        this.examId = examId;
        this.category = category;
    }

    public void update(Long examId, Category category) {
        this.examId = examId;
        this.category = category;
    }
}

