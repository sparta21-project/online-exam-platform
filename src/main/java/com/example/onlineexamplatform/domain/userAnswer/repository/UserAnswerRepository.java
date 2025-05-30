package com.example.onlineexamplatform.domain.userAnswer.repository;

import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
}
