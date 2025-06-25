package com.example.onlineexamplatform.domain.statistics.repository;

import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 통계 관련 쿼리를 위한 레포지토리
 */
@Repository
public interface StatisticsRepository extends JpaRepository<AnswerSheet, Long> {

	@Query("""
			    SELECT AVG(a.score)
			    FROM AnswerSheet a
			    WHERE a.exam.id = :examId AND a.status = 'GRADED'
			""")
	Double getAverageScoreByExam(Long examId);

	@Query(value = """
			    SELECT ea.question_number,
			           SUM(CASE WHEN ua.answer_text = ea.correct_answer THEN 1 ELSE 0 END) * 1.0 / COUNT(*)
			    FROM user_answer ua
			    JOIN answer_sheet ash ON ua.answer_sheet_id = ash.id
			    JOIN exam e ON ash.exam_id = e.id
			    JOIN exam_answer ea ON ea.exam_id = e.id AND ea.question_number = ua.question_number
			    WHERE e.id = :examId
			    GROUP BY ea.question_number
			""", nativeQuery = true)
	List<Object[]> getCorrectRatePerQuestion(Long examId);
}