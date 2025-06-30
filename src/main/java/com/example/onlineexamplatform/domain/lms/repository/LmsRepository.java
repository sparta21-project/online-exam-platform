package com.example.onlineexamplatform.domain.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.lms.entity.Lms;
import com.example.onlineexamplatform.domain.user.entity.User;

public class LmsRepository extends JpaRepository<Lms, Long> {

	List<Lms> findAllByUser(User user);

	List<Lms> findAllByExam(Exam exam);
}
