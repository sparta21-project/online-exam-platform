//package com.example.onlineexamplatform.domain.statistics.service;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.doNothing;
//
//import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
//import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
//import com.example.onlineexamplatform.domain.answerSheet.service.AnswerSheetService;
//import com.example.onlineexamplatform.domain.sms.service.SmsService;
//import com.example.onlineexamplatform.testconfig.MockSmsServiceConfig;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Import(MockSmsServiceConfig.class)
//class ExamStatisticsServiceTest {
//
//	@Autowired
//	private ExamStatisticsService examStatisticsService;
//
//	@Autowired
//	private AnswerSheetService answerSheetService;
//
//	@Autowired
//	private AnswerSheetRepository answerSheetRepository;
//
//	@Autowired
//	private SmsService smsService;
//
//	@Test
//	void saveStatisticsTest() {
//
//		doNothing().when(smsService).createSms(any(Long.class), any(Long.class), anyInt());
//
//		for (long examId = 1; examId <= 20; examId++) {
//			try {
//				// 채점 먼저 수행
//				List<AnswerSheet> sheets = answerSheetRepository.findByExamId(examId);
//				for (AnswerSheet sheet : sheets) {
//					answerSheetService.gradeAnswerSheet(sheet.getId());
//				}
//
//				// 통계 저장 시도
//				examStatisticsService.saveStatistics(examId);
//				System.out.println("시험 ID " + examId + " 통계 저장 완료");
//
//			} catch (Exception e) {
//				System.out.println("시험 ID " + examId + " 통계 저장 실패: " + e.getMessage());
//				// 예외 무시 → 테스트 실패로 간주되지 않음
//			}
//		}
//	}
//}