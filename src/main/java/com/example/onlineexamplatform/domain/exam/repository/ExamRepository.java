package com.example.onlineexamplatform.domain.exam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

	default Exam findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));
	}

	@Query("SELECT e FROM Exam e WHERE e.title LIKE %:examTitle%")
	Page<Exam> findByTitle(Pageable pageable, String examTitle);

	List<Exam> findByEndTimeBefore(LocalDateTime now);

}
