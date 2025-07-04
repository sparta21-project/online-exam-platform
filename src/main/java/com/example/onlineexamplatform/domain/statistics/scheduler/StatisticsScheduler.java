package com.example.onlineexamplatform.domain.statistics.scheduler;

import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.statistics.service.ExamStatisticsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsScheduler {

	private final ExamRepository examRepository;
	private final ExamStatisticsService examStatisticsService;

	/**
	 * 시험 통계 자동 계산 스케줄러 매 시간 10분마다 실행됨 (cron: "0 10 * * * *") 통계가 아직 계산되지 않은 채점 완료 시험들을 대상으로 통계 저장
	 * 처리 실패한 시험은 로그로 기록되며 개별 try-catch로 다음 시험에 영향 없음
	 */
	@Scheduled(cron = "0 10 * * * *")
	public void calculateStatistics() {
		List<Exam> exams = examRepository.findFullyGradedAndNotYetStatistical();

		for (Exam exam : exams) {
			try {
				examStatisticsService.saveStatistics(exam.getId());
				log.info("통계 계산 완료 - 시험 ID: {}", exam.getId());
			} catch (Exception e) {
				log.error("통계 계산 실패 - 시험 ID: {}", exam.getId(), e);
			}
		}
	}
}
