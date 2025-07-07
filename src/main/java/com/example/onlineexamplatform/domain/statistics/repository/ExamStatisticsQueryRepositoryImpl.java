package com.example.onlineexamplatform.domain.statistics.repository;

import static com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus.GRADED;

import com.example.onlineexamplatform.domain.answerSheet.entity.QAnswerSheet;
import com.example.onlineexamplatform.domain.exam.entity.QExam;
import com.example.onlineexamplatform.domain.examAnswer.entity.QExamAnswer;
import com.example.onlineexamplatform.domain.statistics.dto.request.admin.AdminExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.common.QuestionSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.user.UserExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.entity.QExamStatistics;
import com.example.onlineexamplatform.domain.userAnswer.entity.QUserAnswer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


/**
 * 시험 통계 관련 복잡한 조회 쿼리(QueryDSL)를 담당하는 구현체 - 평균 점수, 응시자 수, 문제별 정답률 계산 - 관리자/사용자 조건 기반 통계 목록 조회 -
 * 관리자/사용자 문제 조건 검색
 */
@Repository
@RequiredArgsConstructor
public class ExamStatisticsQueryRepositoryImpl implements ExamStatisticsQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final QExam e = QExam.exam;
	private final QExamStatistics es = QExamStatistics.examStatistics;

	/**
	 * 특정 시험의 평균 점수를 계산 (채점 완료된 답안만 대상)
	 *
	 * @param examId 시험 ID
	 * @return 평균 점수 (소수점 포함)
	 */
	@Override
	public Double getAverageScoreByExam(Long examId) {
		QAnswerSheet ash = QAnswerSheet.answerSheet;

		return queryFactory
				.select(ash.score.avg())
				.from(ash)
				.where(
						ash.exam.id.eq(examId),
						ash.status.eq(GRADED)
				)
				.fetchOne();
	}

	/**
	 * 특정 시험의 채점 완료된 응시자 수를 조회
	 *
	 * @param examId 시험 ID
	 * @return 응시자 수
	 */
	@Override
	public Integer countGradedParticipants(Long examId) {
		QAnswerSheet ash = QAnswerSheet.answerSheet;

		Long count = queryFactory
				.select(ash.count())
				.from(ash)
				.where(
						ash.exam.id.eq(examId),
						ash.status.eq(GRADED)
				)
				.fetchOne();

		return count != null ? count.intValue() : 0;
	}

	/**
	 * 특정 시험에 대한 문제별 정답률을 계산 - 정답 비교는 사용자 답안과 정답지를 기준으로 수행
	 *
	 * @param examId 시험 ID
	 * @return 문제 번호 및 정답률 리스트
	 */
	@Override
	public List<QuestionCorrectRateResponse> getCorrectRatePerQuestion(Long examId) {
		QUserAnswer ua = QUserAnswer.userAnswer;
		QAnswerSheet ash = QAnswerSheet.answerSheet;
		QExamAnswer ea = QExamAnswer.examAnswer;

		List<Tuple> result = queryFactory
				.select(
						ua.questionNumber,
						ua.answerText.when(ea.correctAnswer).then(1).otherwise(0).sum(),
						ua.answerText.count()
				)
				.from(ua)
				.join(ua.answerSheet, ash)
				.join(ea).on(
						ea.exam.id.eq(ash.exam.id),
						ea.questionNumber.eq(ua.questionNumber)
				)
				.where(ash.exam.id.eq(examId))
				.groupBy(ua.questionNumber)
				.fetch();

		return result.stream()
				.map(t -> {
					Integer questionNumber = t.get(ua.questionNumber);
					Integer correctCount = t.get(1, Integer.class);
					Long totalCount = t.get(2, Long.class);

					int correctRate =
							(correctCount == null || totalCount == null || totalCount == 0)
									? 0
									: (int) Math.round((double) correctCount / totalCount * 100);

					return new QuestionCorrectRateResponse(questionNumber, correctRate);
				})
				.toList();
	}

	/**
	 * 관리자 전용 조건(제목, 날짜, 평균 점수, 응시자 수, 공개 여부) 기반 시험 통계 목록 조회
	 *
	 * @param condition 관리자 검색 조건 DTO
	 * @return 관리자용 시험 통계 응답 리스트
	 */
	@Override
	public List<AdminExamStatisticsSearchResponse> searchAdminExamStatisticsByCondition(
			AdminExamStatisticsSearchRequest condition) {

		return queryFactory
				.select(Projections.constructor(
						AdminExamStatisticsSearchResponse.class,
						e.id,
						es.isPublic,
						e.startTime,
						e.endTime,
						e.title,
						es.participantCount,
						es.averageScore
				))
				.from(es)
				.join(es.exam, e)
				.where(
						containsTitle(condition.examTitle()),
						scoreGoe(condition.scoreGte()),
						scoreLoe(condition.scoreLte()),
						examPeriodFilter(condition.examStartDate(),
								condition.examEndDate()),
						participantCountGoe(condition.participantCountGte()),
						participantCountLoe(condition.participantCountLte()),
						isPublicEq(condition.isPublic())
				)
				.fetch();
	}

	/**
	 * 사용자 전용 조건(제목, 날짜, 평균 점수, 응시자 수) 기반 공개된 시험 통계 목록 조회
	 *
	 * @param condition 사용자 검색 조건 DTO
	 * @return 사용자용 시험 통계 응답 리스트
	 */
	@Override
	public List<UserExamStatisticsSearchResponse> searchUserExamStatisticsByCondition(
			UserExamStatisticsSearchRequest condition) {

		return queryFactory
				.select(Projections.constructor(
						UserExamStatisticsSearchResponse.class,
						e.id,
						e.startTime,
						e.endTime,
						e.title,
						es.participantCount,
						es.averageScore
				))
				.from(es)
				.join(es.exam, e)
				.where(
						containsTitle(condition.examTitle()),
						scoreGoe(condition.scoreGte()),
						scoreLoe(condition.scoreLte()),
						examPeriodFilter(condition.examStartDate(),
								condition.examEndDate()),
						participantCountGoe(condition.participantCountGte()),
						participantCountLoe(condition.participantCountLte()),
						es.isPublic.isTrue()
				)
				.fetch();
	}

	/**
	 * 시험 제목 like 검색 조건
	 */
	private BooleanExpression containsTitle(String title) {
		return title != null ? e.title.containsIgnoreCase(title) : null;
	}

	/**
	 * 평균 점수 ≥ 조건
	 */
	private BooleanExpression scoreGoe(Integer scoreGe) {
		return scoreGe != null ? es.averageScore.goe(scoreGe) : null;
	}

	/**
	 * 평균 점수 ≤ 조건
	 */
	private BooleanExpression scoreLoe(Integer scoreLe) {
		return scoreLe != null ? es.averageScore.loe(scoreLe) : null;
	}

	/***
	 * 응시자 수  ≥ 조건
	 */
	private BooleanExpression participantCountGoe(Integer participantCountGe) {
		return participantCountGe != null ? es.participantCount.goe(participantCountGe) : null;
	}

	/***
	 * 응시자 수 ≤ 조건
	 */
	private BooleanExpression participantCountLoe(Integer participantCountLe) {
		return participantCountLe != null ? es.participantCount.loe(participantCountLe) : null;
	}

	/**
	 * 시험 기간 조건 필터링 - 시작일만 입력된 경우: 해당 날짜 이후 시작된 시험 - 종료일만 입력된 경우: 해당 날짜 이전에 종료된 시험 - 둘 다 입력된 경우: 해당
	 * 기간 안에 시행된 시험 (startTime ≥ start && endTime ≤ end)
	 */
	private BooleanExpression examPeriodFilter(LocalDate start, LocalDate end) {
		if (start != null && end != null) {
			return e.startTime.goe(start.atStartOfDay())
					.and(e.endTime.loe(end.atTime(23, 59, 59)));
		} else if (start != null) {
			return e.startTime.goe(start.atStartOfDay());
		} else if (end != null) {
			return e.endTime.loe(end.atTime(23, 59, 59));
		}
		return null;
	}

	/**
	 * 문제 정답률 조건(having 절) 생성 빌더
	 *
	 * @param condition   검색 조건
	 * @param correctRate 정답률 식
	 * @return having 절
	 */
	private BooleanBuilder buildCorrectRateHaving(QuestionSearchRequest condition,
			NumberExpression<Integer> correctRate) {
		BooleanBuilder havingBuilder = new BooleanBuilder();
		if (condition.correctRateGte() != null) {
			havingBuilder.and(correctRate.goe(condition.correctRateGte()));
		}
		if (condition.correctRateLte() != null) {
			havingBuilder.and(correctRate.loe(condition.correctRateLte()));
		}
		return havingBuilder;
	}

	/**
	 * 문제 조건 검색 공통 로직 (관리자/사용자 구분 포함)
	 *
	 * @param condition 검색 조건 DTO
	 * @param isAdmin   관리자 여부
	 * @return 문제 조건 검색 응답 리스트
	 */
	private List<QuestionCorrectRateSearchResponse> searchCorrectRateCommon(
			QuestionSearchRequest condition, boolean isAdmin) {
		QUserAnswer ua = QUserAnswer.userAnswer;
		QAnswerSheet ash = QAnswerSheet.answerSheet;
		QExam exam = QExam.exam;
		QExamAnswer ea = QExamAnswer.examAnswer;
		QExamStatistics es = QExamStatistics.examStatistics;

		NumberExpression<Integer> correctCount = ua.answerText.when(ea.correctAnswer).then(1)
				.otherwise(0).sum();
		NumberExpression<Long> totalCount = ua.answerText.count();
		NumberExpression<Integer> correctRate = correctCount.multiply(100).divide(totalCount)
				.intValue();

		BooleanBuilder havingBuilder = buildCorrectRateHaving(condition, correctRate);

		return queryFactory
				.select(Projections.constructor(
						QuestionCorrectRateSearchResponse.class,
						exam.id,
						exam.startTime,
						exam.endTime,
						exam.title,
						ua.questionNumber,
						correctRate
				))
				.from(ua)
				.join(ua.answerSheet, ash)
				.join(ash.exam, exam)
				.join(es).on(es.exam.eq(exam))
				.join(ea).on(
						ea.exam.id.eq(exam.id),
						ea.questionNumber.eq(ua.questionNumber)
				)
				.where(
						examPeriodFilter(condition.examStartDate(),
								condition.examEndDate()),
						containsTitle(condition.examTitle()),
						isAdmin ? null : es.isPublic.isTrue()
				)
				.groupBy(exam.id, exam.title, ua.questionNumber)
				.having(havingBuilder)
				.fetch();
	}

	/**
	 * 사용자 전용 문제 정답률 조건 검색 (공개된 시험만 대상)
	 *
	 * @param condition 검색 조건 DTO
	 * @return 문제 조건 검색 응답 리스트
	 */
	@Override
	public List<QuestionCorrectRateSearchResponse> searchQuestionsByConditionForAdmin(
			QuestionSearchRequest condition) {
		return searchCorrectRateCommon(condition, true);
	}

	@Override
	public List<QuestionCorrectRateSearchResponse> searchQuestionsByConditionForUser(
			QuestionSearchRequest condition) {
		return searchCorrectRateCommon(condition, false);
	}

	/**
	 * 공개 여부 equals 조건
	 */
	private BooleanExpression isPublicEq(Boolean isPublic) {
		return isPublic != null ? es.isPublic.eq(isPublic) : null;
	}


}
