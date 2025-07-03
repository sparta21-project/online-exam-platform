package com.example.onlineexamplatform.domain.examFile.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.examFile.entity.ExamFile;

public interface ExamFileRepository extends JpaRepository<ExamFile, Long> {

	default ExamFile findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ApiException(ErrorStatus.NOT_EXIST_FILE));
	}

	List<ExamFile> findByExamId(Long examId);
}
