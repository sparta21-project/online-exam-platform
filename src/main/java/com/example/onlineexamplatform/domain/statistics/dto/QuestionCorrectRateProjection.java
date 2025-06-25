package com.example.onlineexamplatform.domain.statistics.dto;

/**
 * Native Query 반환용 Projection Interface
 */
public interface QuestionCorrectRateProjection {
	Integer getQuestionNumber();
	Double getCorrectRate();
}
