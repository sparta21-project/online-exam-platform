package com.example.onlineexamplatform.domain.userCategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserCategoryResponse(

		@Schema(description = "응시 권한 ID", example = "10")
		Long userCategoryId,

		@Schema(description = "응시 권한을 가진 사용자 ID", example = "5")
		Long userId,

		@Schema(description = "응시 권한이 부여된 카테고리 타입", example = "MATH")
		CategoryType categoryType

) {}
