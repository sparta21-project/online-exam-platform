package com.example.onlineexamplatform.domain.userCategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;

public record UserCategoryResponse(
		Long userCategoryId,
		Long userId,
		CategoryType categoryType
) {}
