package com.example.onlineexamplatform.domain.userCategory.dto;

import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import jakarta.validation.constraints.NotNull;

public record UserCategoryRequest(
		@NotNull CategoryType categoryType
) {}
