package com.example.onlineexamplatform.domain.examCategory.repository;

import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamCategoryRepository extends JpaRepository<ExamCategory, Long> {
}
