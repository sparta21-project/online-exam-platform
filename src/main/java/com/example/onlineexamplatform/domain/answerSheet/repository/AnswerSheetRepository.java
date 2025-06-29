package com.example.onlineexamplatform.domain.answerSheet.repository;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {
    Optional<AnswerSheet> findById(Long id);
    List<AnswerSheet> findByExamId(Long examId);
    Optional<AnswerSheet> findByExamAndUser(Exam exam, User user);
}