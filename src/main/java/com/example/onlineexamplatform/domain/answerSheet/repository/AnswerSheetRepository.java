package com.example.onlineexamplatform.domain.answerSheet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.user.entity.User;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {
	Optional<AnswerSheet> findById(Long id);

	List<AnswerSheet> findByExamId(Long examId);

	Optional<AnswerSheet> findTopByExamAndUserAndStatusInOrderByCreatedAt(Exam exam, User user,
		List<AnswerSheetStatus> includedStatuses
	);

	List<AnswerSheet> findByExamIdAndStatusNotIn(Long examId, List<AnswerSheetStatus> statuses);

	List<AnswerSheet> findByExamIdAndStatusNot(Long examId, AnswerSheetStatus status);
}