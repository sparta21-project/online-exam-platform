package com.example.onlineexamplatform.domain.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문제별 정답률 통계 엔티티 - 하나의 시험 통계(ExamStatistics)에 여러 문제 통계가 존재함 (N:1) - 각 문제 번호에 대한 정답률을 0~100의 정수로 저장
 */

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "exam_question_statistics")
public class ExamQuestionStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_statistics_id", nullable = false)
	private ExamStatistics examStatistics;

	@Column(nullable = false)
	private Integer questionNumber;

	@Column(nullable = false)
	private Integer correctRate;

	public static ExamQuestionStatistics of(ExamStatistics examStatistics, Integer questionNumber,
			Integer correctRate) {
		return ExamQuestionStatistics.builder()
				.examStatistics(examStatistics)
				.questionNumber(questionNumber)
				.correctRate(correctRate)
				.build();
	}
}
