package com.example.onlineexamplatform.domain.statistics.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ExamStatisticsServiceTest {

	@Autowired
	private ExamStatisticsService examStatisticsService;

	@Test
	void saveStatisticsTest() {
		for (long examId = 1; examId <= 5; examId++) {
			examStatisticsService.saveStatistics(examId);
			System.out.println("시험 ID " + examId + " 통계 저장 완료");
		}
	}
}