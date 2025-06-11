package com.example.onlineexamplatform.domain.examCategory.dto;

import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ExamServiceRequestDto {

	@NotNull(message = "ID는 필수입니다")
	private Long id;

	private Long examId;

	@NotNull(message = "카테고리 타입은 필수입니다")
	private ExamCategory examCategory;

}
