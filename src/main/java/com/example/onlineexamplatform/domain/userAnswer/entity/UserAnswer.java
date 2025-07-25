package com.example.onlineexamplatform.domain.userAnswer.entity;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "user_answer")
@NoArgsConstructor
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "answer_sheet_id")
    private AnswerSheet answerSheet;

    @Column(nullable = false)
    private Integer questionNumber;

    @Column(nullable = false)
    private String answerText;

    public UserAnswer(AnswerSheet answerSheet, int questionNumber, String answerText) {
        this.answerSheet = answerSheet;
        this.questionNumber = questionNumber;
        this.answerText = answerText;
    }

    public void updateAnswer(String answerText) {
        this.answerText = answerText;
    }
}
