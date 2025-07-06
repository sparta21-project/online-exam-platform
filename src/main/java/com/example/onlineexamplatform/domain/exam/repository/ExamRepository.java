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

	/**
	 * 채점 완료된 시험 중 종료 시간이 주어진 기준 시각(threshold) 이후인 시험 목록 조회. 모든 답안이 GRADED 상태인 시험만 대상으로 함. 통계 스케줄러에서
	 * 최근 시험 통계 계산 시 사용.
	 */
	@Query("""
				SELECT e FROM Exam e
				WHERE EXISTS (
					SELECT a FROM AnswerSheet a
					WHERE a.exam = e AND a.status = 'GRADED'
				)
				AND e.endTime >= :threshold
			""")
	List<Exam> findGradedExamsAfter(LocalDateTime threshold);

	/**
	 * 채점 완료된 시험 중 종료 시간이 주어진 기준 시각(threshold) 이전인 시험 목록 조회 .모든 답안이 GRADED 상태인 시험만 대상으로 함 .통계 스케줄러에서
	 * 과거 시험 통계 재보정 시 사용
	 */
	@Query("""
				SELECT e FROM Exam e
				WHERE EXISTS (
					SELECT a FROM AnswerSheet a
					WHERE a.exam = e AND a.status = 'GRADED'
				)
				AND e.endTime < :threshold
			""")
	List<Exam> findGradedExamsBefore(LocalDateTime threshold);


}
