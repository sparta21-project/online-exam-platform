package com.example.onlineexamplatform.domain.examcategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onlineexamplatform.domain.examcategory.entity.ExamCategoryEntity;

@Repository
public interface ExamCategoryRepository extends JpaRepository<ExamCategoryEntity, Long> {

	// 특정 시험(examId)에 해당하는 카테고리 전체 조회
	List<ExamCategoryEntity> findByExamId(Long examId);

	// // 특정 시험과 카테고리 타입으로 조회
	// ExamCategoryEntity findByExamIdAndCategoryType(Long examId, ExamCategory categoryType);
	//
	// // 카테고리 타입별로 전체 조회
	// List<ExamCategoryEntity> findByCategoryType(ExamCategory categoryType);
	//
	// // 존재 여부 확인
	// boolean existsByExamIdAndCategoryType(Long examId, ExamCategory categoryType);
}
