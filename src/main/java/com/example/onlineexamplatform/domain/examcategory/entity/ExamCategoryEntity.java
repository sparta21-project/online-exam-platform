package com.example.onlineexamplatform.domain.examcategory.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ExamCategoryEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long examId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ExamCategory categoryType;

	public ExamCategoryEntity(Long examId, ExamCategory categoryType) {
		this.examId = examId;
		this.categoryType = categoryType;
	}

	public void update(Long id, Long examId, ExamCategory categoryType) {
		this.id = id;
		this.examId = examId;
		this.categoryType = categoryType;
	}
}

