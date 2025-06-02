package com.example.onlineexamplatform.domain.category;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.entity.CategoryType;
import com.example.onlineexamplatform.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
/**
 * 애플리케이션 실행시 enum에 정의한 값들을
 * category 테이블에 해당 값이 존재하지 않으면 자동으로 저장함.
 */
@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {

	private final CategoryRepository categoryRepository;

	@Override
	public void run(String... args) {
		for (CategoryType type : CategoryType.values()) {
			categoryRepository.findByCategoryType(type)
					.orElseGet(() -> categoryRepository.save(new Category(type)));
		}
	}
}
