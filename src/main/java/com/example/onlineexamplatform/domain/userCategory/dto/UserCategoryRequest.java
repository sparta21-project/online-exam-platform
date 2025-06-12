package com.example.onlineexamplatform.domain.userCategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserCategoryRequest(
		@NotNull
		@Schema(description = "응시 권한을 부여할 카테고리 타입", example = "MATH")
		CategoryType categoryType
) {}
