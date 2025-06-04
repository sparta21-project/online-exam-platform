package com.example.onlineexamplatform.domain.examcategory.repository;

import com.example.onlineexamplatform.domain.examcategory.entity.ExamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamCategoryRepository extends JpaRepository<ExamCategory, Long> {

    // 특정 시험(examId)에 해당하는 카테고리 전체 조회
    List<ExamCategory> findByExamId(Long examId);

    List<ExamCategory> findByExamIdAndCategoryId(Long examId, Long categoryId);


}
