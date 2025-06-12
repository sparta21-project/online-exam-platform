package com.example.onlineexamplatform.domain.examFile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;

public interface ExamFileRepository extends JpaRepository<ExamFile, Long> {

	List<ExamFile> findByExamId(Long examId);
}
