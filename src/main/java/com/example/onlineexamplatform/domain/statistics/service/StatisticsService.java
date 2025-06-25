package com.example.onlineexamplatform.domain.statistics.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.statistics.dto.ExamStatisticsDto;
import com.example.onlineexamplatform.domain.statistics.dto.QuestionCorrectRateDto;
import com.example.onlineexamplatform.domain.statistics.dto.QuestionCorrectRateProjection;
import com.example.onlineexamplatform.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 시험 통계 비즈니스 로직 처리
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

	private final StatisticsRepository statisticsRepository;
	private final ExamRepository examRepository;

	public ExamStatisticsDto getExamStatistics(Long examId) {
		examRepository.findByIdOrElseThrow(examId);

		Double averageScoreRaw = statisticsRepository.getAverageScoreByExam(examId);
		if (averageScoreRaw == null) {
			throw new ApiException(ErrorStatus.STATISTICS_NOT_FOUND);
		}
		int averageScore = (int) Math.round(averageScoreRaw);

		List<QuestionCorrectRateDto> questionStats = statisticsRepository
				.getCorrectRatePerQuestion(examId)
				.stream()
				.map(row -> new QuestionCorrectRateDto(
						((Number) row[0]).intValue(),
						(int) Math.round(((Number) row[1]).doubleValue() * 100)
				))
				.toList();

		return new ExamStatisticsDto(averageScore, questionStats);
	}
}
