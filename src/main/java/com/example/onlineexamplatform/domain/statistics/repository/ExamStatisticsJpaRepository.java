package com.example.onlineexamplatform.domain.statistics.repository;

import com.example.onlineexamplatform.domain.statistics.entity.ExamStatistics;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamStatisticsJpaRepository extends JpaRepository<ExamStatistics, Long>,
		ExamStatisticsQueryRepository {

	Optional<ExamStatistics> findByExamIdAndIsPublicTrue(Long examId);

	Optional<ExamStatistics> findByExamId(Long examId);
}
