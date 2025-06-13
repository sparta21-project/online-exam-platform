package com.example.onlineexamplatform.domain.examFile.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@Table(name = "exam_file")
@NoArgsConstructor
@AllArgsConstructor
public class ExamFile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String path;

	@Column(nullable = false)
	private String fileType;

	@Column(nullable = false)
	private Long size;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_id")
	private Exam exam;

	// Exam과 ExamFile 맵핑 메서드
	public void assignExam(Exam exam) {
		this.exam = exam;
	}

}
