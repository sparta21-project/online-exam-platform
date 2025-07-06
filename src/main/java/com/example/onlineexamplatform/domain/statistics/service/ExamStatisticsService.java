package com.example.onlineexamplatform.domain.statistics.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.statistics.dto.request.admin.AdminExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.common.QuestionSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.request.user.UserExamStatisticsSearchRequest;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.admin.AdminExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.common.QuestionCorrectRateSearchResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsResponse;
import com.example.onlineexamplatform.domain.statistics.dto.response.user.UserExamStatisticsSearchResponse;
import com.example.onlineexamplatform.domain.statistics.entity.ExamQuestionStatistics;
import com.example.onlineexamplatform.domain.statistics.entity.ExamStatistics;
import com.example.onlineexamplatform.domain.statistics.repository.ExamStatisticsJpaRepository;
import com.example.onlineexamplatform.domain.statistics.repository.ExamStatisticsQueryRepository;
import com.example.onlineexamplatform.domain.statistics.repository.QuestionStatisticsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 시험 통계와 관련된 비즈니스 로직 처리 서비스. 시험 통계 저장 및 갱신 , 관리자/사용자용 통계 단건 조회 및 조건 검색 , 문제별 정답률 계산 , 통계 공개 여부 변경
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamStatisticsService {

	// QueryDSL 기반 통계 쿼리 구현체 주입
	@Qualifier("examStatisticsQueryRepositoryImpl")
	private final ExamStatisticsQueryRepository statisticsqueryRepository;
	private final QuestionStatisticsRepository detailRepository;
	private final ExamRepository examRepository;
	private final ExamStatisticsJpaRepository examStatisticsJpaRepository;

	/**
	 * 관리자 전용 특정 시험의 통계 정보 단건 조회. 통계가 존재하지 않을 경우 빈 응답 반환
	 *
	 * @param examId 시험 ID
	 * @return 시험 평균 점수, 응시자 수, 문제별 정답률 포함된 응답 DTO
	 */
	public AdminExamStatisticsResponse getExamStatistics(Long examId) {
		var exam = examRepository.findByIdOrElseThrow(examId);

		// 통계 데이터가 없을 수 있으므로 먼저 확인
		var statsOpt = examStatisticsJpaRepository.findByExamId(examId);

		if (statsOpt.isEmpty()) {
			// 통계가 없으면 빈 응답 반환
			return new AdminExamStatisticsResponse(
					false,
					exam.getStartTime(),
					exam.getEndTime(),
					exam.getTitle(),
					0,
					0,
					List.of()
			);
		}

		var stats = statsOpt.get();

		List<QuestionCorrectRateResponse> questionStats = statisticsqueryRepository.getCorrectRatePerQuestion(
				examId);

		return new AdminExamStatisticsResponse(
				stats.getIsPublic(),
				exam.getStartTime(),
				exam.getEndTime(),
				stats.getExam().getTitle(),
				stats.getParticipantCount(),
				stats.getAverageScore(),
				questionStats
		);
	}

	/**
	 * 사용자 전용 공개된 시험의 통계 정보 단건 조회 비공개 통계이거나 존재하지 않으면 예외 발생
	 *
	 * @param examId 시험 ID
	 * @return 시험 제목, 평균 점수, 응시자 수, 문제별 정답률 포함된 응답 DTO
	 */
	public UserExamStatisticsResponse getUserExamStatistics(Long examId) {

		var statsOpt = examStatisticsJpaRepository.findByExamIdAndIsPublicTrue(examId);

		if (statsOpt.isEmpty()) {
			throw new ApiException(ErrorStatus.STATISTICS_NOT_PUBLIC);
		}

		var stats = statsOpt.get();
		var exam = stats.getExam();

		List<QuestionCorrectRateResponse> details = detailRepository.findByExamStatistics(stats)
				.stream()
				.map(d -> new QuestionCorrectRateResponse(d.getQuestionNumber(),
						d.getCorrectRate()))
				.toList();

		return new UserExamStatisticsResponse(
				exam.getStartTime(),
				exam.getEndTime(),
				stats.getExam().getTitle(),
				stats.getParticipantCount(),
				stats.getAverageScore(),
				details
		);
	}

	/**
	 * 관리자/스케줄러 전용 특정 시험의 통계를 새로 계산하고 저장하고 평균 점수, 응시자 수, 정답률 계산 후 기존 데이터와 비교하여 변경이 있을 경우만 갱신
	 *
	 * @param examId 시험 ID
	 */
	@Transactional
	public void saveStatistics(Long examId) {
		var exam = examRepository.findByIdOrElseThrow(examId);
		log.info("[examId={}] 통계 계산 시도", examId);

		// 신규 통계 데이터 계산
		Double averageScoreRaw = statisticsqueryRepository.getAverageScoreByExam(examId);
		int participantCount = statisticsqueryRepository.countGradedParticipants(examId);
		if (averageScoreRaw == null || participantCount == 0) {
			log.warn("[examId={}] 통계 계산 생략 - 채점된 응시자 없음", examId);
			return;
		}

		int averageScore = (int) Math.round(averageScoreRaw);
		List<QuestionCorrectRateResponse> newCorrectRates = statisticsqueryRepository.getCorrectRatePerQuestion(
				examId);

		// 기존 통계가 존재할 경우: 변경 여부 체크
		var existingOpt = examStatisticsJpaRepository.findByExamId(examId);
		if (existingOpt.isPresent()) {
			ExamStatistics existing = existingOpt.get();

			// 평균, 응시자수 비교
			boolean isScoreChanged = existing.getAverageScore() != averageScore;
			boolean isParticipantChanged = existing.getParticipantCount() != participantCount;

			// 정답률 비교
			List<ExamQuestionStatistics> existingDetail = detailRepository.findByExamStatistics(
					existing);
			boolean isCorrectRateChanged = !compareCorrectRates(existingDetail, newCorrectRates);

			// 변경사항 없으면 저장 안함
			if (!isScoreChanged && !isParticipantChanged && !isCorrectRateChanged) {
				log.info("[examId={}] 통계 저장 생략 - 기존 통계와 동일", examId);
				return;
			}

			// 변경사항 있으면 기존 데이터 삭제
			detailRepository.deleteAllInBatch(detailRepository.findByExamStatistics(existing));
			examStatisticsJpaRepository.delete(existing);
			examStatisticsJpaRepository.flush();
		}

		// 통계 저장
		ExamStatistics Statistics = examStatisticsJpaRepository.save(
				ExamStatistics.builder()
						.exam(exam)
						.averageScore(averageScore)
						.participantCount(participantCount)
						.isPublic(false)
						.build()
		);

		// 정답률 저장
		List<ExamQuestionStatistics> detailList = newCorrectRates.stream()
				.map(dto -> ExamQuestionStatistics.of(Statistics, dto.questionNumber(),
						dto.correctRate()))
				.toList();
		detailRepository.saveAll(detailList);
		log.info("[examId={}] 통계 저장 완료 - 평균 점수={}, 응시자 수={}",
				examId, averageScore, participantCount);
	}

	/**
	 * (관리자 전용) 시험 통계의 공개 여부를 변경.  공개 요청 시 응시자 수나 평균 점수가 0이면 예외 발생. 상태 변경이 없는 경우 메시지만 반환
	 *
	 * @param examId           시험 ID
	 * @param targetVisibility true = 공개, false = 비공개
	 * @return 변경 결과 메시지
	 */
	@Transactional
	public String updateVisibility(Long examId, Boolean targetVisibility) {
		ExamStatistics statistics = examStatisticsJpaRepository.findByExamId(examId)
				.orElseThrow(() -> new ApiException(ErrorStatus.STATISTICS_NOT_FOUND));

		if (targetVisibility) {
			if (statistics.getParticipantCount() == 0 || statistics.getAverageScore() == 0) {
				throw new ApiException(ErrorStatus.STATISTICS_EMPTY_DATA);
			}
		}

		if (statistics.getIsPublic().equals(targetVisibility)) {
			return targetVisibility ? "이미 공개 상태입니다." : "이미 비공개 상태입니다.";
		}

		statistics.setIsPublic(targetVisibility);
		return targetVisibility ? "공개 상태로 변경했습니다." : "비공개 상태로 변경했습니다.";
	}

	/**
	 * 관리자 또는 사용자 문제 조건(정답률, 기간 등)에 따라 문제별 정답률 목록 조회
	 *
	 * @param condition 검색 조건
	 * @param isAdmin   관리자 여부
	 * @return 문제별 정답률 응답 DTO 리스트
	 */
	public List<QuestionCorrectRateSearchResponse> searchQuestionsByCondition(
			QuestionSearchRequest condition, boolean isAdmin) {
		return isAdmin
				? statisticsqueryRepository.searchQuestionsByConditionForAdmin(condition)
				: statisticsqueryRepository.searchQuestionsByConditionForUser(condition);
	}

	/**
	 * 사용자 전용 조건(기간, 제목, 점수 등)에 따라 공개된 시험 통계 목록 조회
	 *
	 * @param condition 검색 조건 DTO
	 * @return 사용자용 시험 통계 응답 리스트
	 */
	public List<UserExamStatisticsSearchResponse> searchUserExamStatisticsByCondition(
			UserExamStatisticsSearchRequest condition) {
		return statisticsqueryRepository.searchUserExamStatisticsByCondition(condition);

	}

	/**
	 * 관리자 전용 조건(공개여부 포함)에 따라 모든 시험 통계 목록 조회
	 *
	 * @param condition 검색 조건 DTO
	 * @return 관리자용 시험 통계 응답 리스트
	 */
	public List<AdminExamStatisticsSearchResponse> searchAdminExamStatisticsByCondition(
			AdminExamStatisticsSearchRequest condition) {
		return statisticsqueryRepository.searchAdminExamStatisticsByCondition(condition);
	}

	/**
	 * 내부 유틸 기존 정답률 목록과 새로 계산된 정답률 목록 비교
	 *
	 * @param existing 기존 DB 저장된 정답률
	 * @param fresh    새로 계산된 정답률
	 * @return 변경 여부 (true = 동일, false = 다름)
	 */
	private boolean compareCorrectRates(List<ExamQuestionStatistics> existing,
			List<QuestionCorrectRateResponse> fresh) {
		if (existing.size() != fresh.size()) {
			return false;
		}

		for (ExamQuestionStatistics old : existing) {
			var match = fresh.stream()
					.filter(n -> n.questionNumber().equals(old.getQuestionNumber()))
					.findFirst()
					.orElse(null);

			if (match == null || !old.getCorrectRate().equals(match.correctRate())) {
				return false;
			}

		}
		return true;
	}

}
