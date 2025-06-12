package com.example.onlineexamplatform.domain.category.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시험 카테고리 타입")
public enum CategoryType {

	@Schema(description = "수학")
	MATH,

	@Schema(description = "영어")
	ENGLISH,

	@Schema(description = "과학")
	SCIENCE,

	@Schema(description = "역사")
	HISTORY,

	@Schema(description = "기술")
	TECH
}
