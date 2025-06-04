package com.example.onlineexamplatform.domain.examCategory.repository;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCategoryRepository extends JpaRepository<ExamCategory, Long> {

    List<ExamCategory> findAllByExamId(Long examId);

    List<ExamCategory> findAllByCategoryId(Long categoryId);

    ExamCategory exam(Exam exam);
}
