package com.example.onlineexamplatform.domain.statistics.entity;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시험 통계 엔티티 하나의 시험(Exam)에 대해 통계 데이터를 1:1로 보관 - 평균 점수, 응시자 수, 공개 여부 저장 - createdAt, updatedAt 자동 갱신
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exam_statistics")
public class ExamStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_id", nullable = false, unique = true)
	private Exam exam;

	@Column(nullable = false)
	private Integer averageScore;

	@Column(nullable = false)
	private Integer participantCount;

	@Column(nullable = false)
	private Boolean isPublic;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		this.createdAt = this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
}
