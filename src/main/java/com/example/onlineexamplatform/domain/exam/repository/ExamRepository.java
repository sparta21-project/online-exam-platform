package com.example.onlineexamplatform.domain.exam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

	default Exam findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));
	}

	Page<Exam> findByTitle(Pageable pageable, String examTitle);

}
