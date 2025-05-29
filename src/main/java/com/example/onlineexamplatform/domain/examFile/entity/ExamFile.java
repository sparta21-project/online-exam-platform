package com.example.onlineexamplatform.domain.examFile.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.*;

@Entity
public class ExamFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String path;

    private int size;

    @ManyToOne
    private Exam exam;
}
