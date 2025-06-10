package com.example.onlineexamplatform.domain.userCategory.repository;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

	Optional<UserCategory> findByUserIdAndCategory(Long userId, Category category);

	List<UserCategory> findByUserId(Long userId);
}
