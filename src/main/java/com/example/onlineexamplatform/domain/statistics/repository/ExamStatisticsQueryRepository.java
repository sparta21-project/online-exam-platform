package com.example.onlineexamplatform.domain.statistics.repository;

import com.example.onlineexamplatform.domain.statistics.dto.request.admin.AdminExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.common.QuestionSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.user.UserExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsSearchResponse;
import java.util.List;

public interface ExamStatisticsQueryRepository {

	Double getAverageScoreByExam(Long examId);

	Integer countGradedParticipants(Long examId);

	List<QuestionCorrectRateResponse> getCorrectRatePerQuestion(Long examId);

	List<AdminExamStatisticsSearchResponse> searchAdminExamStatisticsByCondition(
			AdminExamStatisticsSearchRequest condition);

	List<QuestionCorrectRateSearchResponse> searchQuestionsByConditionForAdmin(
			QuestionSearchRequest condition);

	List<QuestionCorrectRateSearchResponse> searchQuestionsByConditionForUser(
			QuestionSearchRequest condition);

	List<UserExamStatisticsSearchResponse> searchUserExamStatisticsByCondition(
			UserExamStatisticsSearchRequest condition);

}
