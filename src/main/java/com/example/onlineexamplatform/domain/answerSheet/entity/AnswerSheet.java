package com.example.onlineexamplatform.domain.answerSheet.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answersheet")
public class AnswerSheet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerSheetStatus status;

    public AnswerSheet(Exam exam, User user, AnswerSheetStatus status){
        this.exam = exam;
        this.user = user;
        this.status = status;
    }

    public void updateStatus(AnswerSheetStatus status) {
        this.status = status;
    }

    public void grade(int score) {
        this.score = score;
        this.status = AnswerSheetStatus.GRADED;
    }
}