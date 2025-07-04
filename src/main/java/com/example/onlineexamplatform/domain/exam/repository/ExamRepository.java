package com.example.onlineexamplatform.domain.exam.repository;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExamRepository extends JpaRepository<Exam, Long> {

	default Exam findByIdOrElseThrow(Long id) {
		return findById(id).orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));
	}

	@Query("SELECT e FROM Exam e WHERE e.title LIKE %:examTitle%")
	Page<Exam> findByTitle(Pageable pageable, String examTitle);

	List<Exam> findByEndTimeBefore(LocalDateTime now);

	/***
	 * 통계가 아직 생성되지 않았고,
	 * 모든 답안이 GRADED 상태인 시험만 조회하여 통계 계산 대상으로 사용 (스케쥴러용)
	 *
	 */
	@Query("""
				SELECT e FROM Exam e
				WHERE NOT EXISTS (
					SELECT s FROM ExamStatistics s
					WHERE s.exam = e
				)
				AND NOT EXISTS (
					SELECT a FROM AnswerSheet a
					WHERE a.exam = e AND a.status != 'GRADED'
				)
			""")
	List<Exam> findFullyGradedAndNotYetStatistical();


}
