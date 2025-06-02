package com.example.onlineexamplatform.domain.category.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.entity.CategoryType;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryType(CategoryType categoryType);
}
