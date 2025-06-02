package com.example.onlineexamplatform.domain.examcategory.dto;

import com.example.onlineexamplatform.domain.examcategory.entity.ExamCategory;

import lombok.Getter;

@Getter
public class ExamServiceResponseDto {

	private final Long id;
	private final Long examId;
	private final ExamCategory categoryType;

	public ExamServiceResponseDto(Long id, Long examId, ExamCategory categoryType) {
		this.id = id;
		this.examId = examId;
		this.categoryType = categoryType;
	}
}
