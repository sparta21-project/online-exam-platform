package com.example.onlineexamplatform.domain.userCategory.dto;

import com.example.onlineexamplatform.common.validator.ValidEnum;
import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserCategoryRequest(

		@NotBlank(message = "카테고리 타입은 필수입니다.")
		@ValidEnum(enumClass = CategoryType.class, message = "존재하지 않는 카테고리입니다.")
		@Schema(description = "응시 권한을 부여할 카테고리 타입", example = "MATH")
		String categoryType

) {}
