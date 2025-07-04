package com.example.onlineexamplatform.domain.statistics.repository;

import com.example.onlineexamplatform.domain.statistics.entity.ExamQuestionStatistics;
import com.example.onlineexamplatform.domain.statistics.entity.ExamStatistics;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionStatisticsRepository extends
		JpaRepository<ExamQuestionStatistics, Long> {

	List<ExamQuestionStatistics> findByExamStatistics(ExamStatistics statistics);
}
