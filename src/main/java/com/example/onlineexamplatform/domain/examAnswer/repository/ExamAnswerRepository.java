package com.example.onlineexamplatform.domain.examAnswer.repository;

import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {
    Page<ExamAnswer> findAllByExamId(Long examId, Pageable pageable);

    List<ExamAnswer> findAllByExamId(Long examId);

    int countByExamId(Long examId);
}
