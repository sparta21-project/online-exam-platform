package com.example.onlineexamplatform.domain.statistics.scheduler;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.statistics.service.ExamStatisticsService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 시험 통계 자동 계산 및 저장 스케줄러. 통계가 아직 계산되지 않은 채점 완료 시험들을 대상으로 지정된 시간에 통계 계산 및 저장. 실행과 결과는 로그로 기록됨
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsScheduler {

	private final ExamRepository examRepository;
	private final ExamStatisticsService examStatisticsService;

	/**
	 * [1] 최근 종료된 시험 (3일 이내) → 매시간 21분마다 실행
	 */
	@Scheduled(cron = "0 21 * * * *")
	public void calculateRecentExamStatistics() {

		LocalDateTime recentThreshold = LocalDateTime.now().minusDays(3);
		log.info("[기준일:{}] 최근 통계 스케줄러 실행 시작", recentThreshold);
		List<Exam> recentExams = examRepository.findGradedExamsAfter(recentThreshold);

		for (Exam exam : recentExams) {
			try {
				examStatisticsService.saveStatistics(exam.getId());
				log.info("[examId={}] 최근 통계 스케줄러 완료", exam.getId());
			} catch (Exception e) {
				log.error("[examId={}] 최근 통계 스케줄러 실패 - {}", exam.getId(), e.getMessage(), e);
			}
		}
	}

	/**
	 * [2] 과거 종료된 시험(3일 이전) → 매주 일요일 오후 8시 18분에 실행
	 */
	@Scheduled(cron = "0 18 18 * * SUN")
	public void recalculateOldExamStatistics() {

		LocalDateTime oldThreshold = LocalDateTime.now().minusDays(3);
		log.info("[기준일:{}] 과거 통계 스케줄러 실행 시작", oldThreshold);
		List<Exam> oldExams = examRepository.findGradedExamsBefore(oldThreshold);

		for (Exam exam : oldExams) {
			try {
				examStatisticsService.saveStatistics(exam.getId());
				log.info("[examId={}] 과거 통계 스케줄러 완료", exam.getId());
			} catch (Exception e) {
				log.error("[examId={}] 과거 통계 스케줄러 실패 - {}", exam.getId(), e.getMessage(), e);
			}
		}
	}
}



