package com.example.onlineexamplatform.domain.examAnswer.entity;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Exam_answer")
@NoArgsConstructor
public class ExamAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_id")
	private Exam exam;

	@Column(nullable = false)
	private Integer questionNumber;

	@Column(nullable = false)
	private Integer questionScore;

	@Column(nullable = false)
	private String correctAnswer;

	public ExamAnswer(Exam exam, Integer questionNumber, Integer questionScore,
			String correctAnswer) {
		this.exam = exam;
		this.questionNumber = questionNumber;
		this.questionScore = questionScore;
		this.correctAnswer = correctAnswer;
	}

	public void updateExamAnswer(Integer questionScore, String correctAnswer) {
		this.questionScore = questionScore;
		this.correctAnswer = correctAnswer;
	}
}
