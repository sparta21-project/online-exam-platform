package com.example.onlineexamplatform.domain.userAnswer.repository;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findAllByAnswerSheetId(Long answerSheetId);
    Optional<UserAnswer> findByAnswerSheetAndQuestionNumber(AnswerSheet answerSheet, int questionNumber);
    List<UserAnswer> findAllByAnswerSheet(AnswerSheet answerSheet);
    void deleteAllByAnswerSheet(AnswerSheet answerSheet);

    Optional<UserAnswer> findByAnswerSheetIdAndQuestionNumber(Long answerSheetId, Integer questionNumber);
}
