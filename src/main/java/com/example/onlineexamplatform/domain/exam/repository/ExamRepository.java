package com.example.onlineexamplatform.domain.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.exam.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

	// default Exam findByIdOrElseThrow(Long id) {
	// 	return findById(id).orElseThrow(()-> new ApiException());
	// }

}
