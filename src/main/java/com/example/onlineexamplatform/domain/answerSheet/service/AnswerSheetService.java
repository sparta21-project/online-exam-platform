package com.example.onlineexamplatform.domain.answerSheet.service;

import static com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus.STARTED;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.AnswerSheetRequestDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.request.UserAnswerRequestDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.AnswerSheetResponseDto;
import com.example.onlineexamplatform.domain.answerSheet.dto.response.UserAnswerResponseDto;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.enums.AnswerSheetStatus;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;
import com.example.onlineexamplatform.domain.examCategory.entity.ExamCategory;
import com.example.onlineexamplatform.domain.examCategory.repository.ExamCategoryRepository;
import com.example.onlineexamplatform.domain.sms.service.SmsService;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.entity.User;
import com.example.onlineexamplatform.domain.user.repository.UserRepository;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import com.example.onlineexamplatform.domain.userCategory.entity.UserCategory;
import com.example.onlineexamplatform.domain.userCategory.repository.UserCategoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerSheetService {

	private final AnswerSheetRepository answerSheetRepository;
	private final UserAnswerRepository userAnswerRepository;
	private final ExamRepository examRepository;
	private final UserRepository userRepository;
	private final ExamAnswerRepository examAnswerRepository;
	private final ExamCategoryRepository examCategoryRepository;
	private final UserCategoryRepository userCategoryRepository;
	private final SmsService smsService;

	//빈 답안지 생성 (시험 응시)
	@Transactional
	public void createAnswerSheet(Long examId, Long userId) {
		Exam exam = examRepository.findByIdOrElseThrow(examId);
		log.info("📌 [User {}] 조회한 remainUsers: {}", userId, exam.getRemainUsers());

		exam.decreaseRemainUsers();
		log.info("🔻 [User {}] 감소 후 remainUsers: {}", userId, exam.getRemainUsers());

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		List<ExamCategory> examCategories = examCategoryRepository.findAllByExamId(examId);
		List<UserCategory> userCategories = userCategoryRepository.findByUserId(userId);

		//답안지 생성을 하나로 제한
		boolean exists = answerSheetRepository.existsByExamAndUser(exam, user);
		if (exists) {
			throw new ApiException(ErrorStatus.ANSWER_SHEET_ALREADY_EXISTS);
		}

		boolean hasCategory = examCategories.isEmpty();
		//카테고리가 없으면 누구든 응시 가능
		for (ExamCategory examCategory : examCategories) {
			for (UserCategory userCategory : userCategories) {
				if (examCategory.getCategory().getId().equals(userCategory.getCategory().getId())) {
					hasCategory = true;
					break;
				}
			}
			if (hasCategory) {
				break;
			}
		}

		if (hasCategory) {
			AnswerSheet answerSheet = new AnswerSheet(exam, user, STARTED);
			answerSheetRepository.save(answerSheet);
		} else {
			throw new ApiException(ErrorStatus.CATEGORY_NOT_MATCHED);
		}
	}

	//답안지 수정 (임시 저장 포함)
	@Transactional
	public AnswerSheetResponseDto.Update updateAnswerSheet(Long examId,
			AnswerSheetRequestDto requestDto, Long userId) {
		Exam exam = examRepository.findByIdOrElseThrow(examId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		AnswerSheet answerSheet = answerSheetRepository.findByExamAndUser(exam, user)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		if (answerSheet.getStatus() == AnswerSheetStatus.SUBMITTED
				|| answerSheet.getStatus() == AnswerSheetStatus.GRADED) {
			throw new ApiException(ErrorStatus.ANSWER_SUBMITTED);
		}

		saveUserAnswers(requestDto, answerSheet);

		answerSheet.updateStatus(AnswerSheetStatus.IN_PROGRESS);

		List<UserAnswer> userAnswers = userAnswerRepository.findAllByAnswerSheet(answerSheet);

		List<UserAnswerResponseDto> answerDtos = sortUserAnswers(userAnswers);

		return new AnswerSheetResponseDto.Update(
				exam.getId(),
				user.getId(),
				answerSheet.getStatus(),
				answerDtos
		);
	}

	//답안지 조회 (본인)
	@Transactional(readOnly = true)
	public AnswerSheetResponseDto.Get getAnswerSheet(Long examId, Long answerSheetId, Long userId) {
		Exam exam = examRepository.findByIdOrElseThrow(examId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		//본인이나 관리자가 아니면 에러
		if (!answerSheet.getExam().getId().equals(exam.getId()) ||
				!(answerSheet.getUser().getId().equals(user.getId()) || user.getRole()
						.equals(Role.ADMIN))) {
			throw new ApiException(ErrorStatus.ACCESS_DENIED);
		}

		List<UserAnswer> userAnswers = userAnswerRepository.findAllByAnswerSheet(answerSheet);

		List<UserAnswerResponseDto> answerDtos = sortUserAnswers(userAnswers);

		return new AnswerSheetResponseDto.Get(
				exam.getId(),
				user.getId(),
				answerSheet.getStatus(),
				answerDtos
		);
	}

	//답안지 삭제
	@Transactional
	public void deleteAnswerSheet(Long examId, Long answerSheetId, Long userId) {
		examRepository.findByIdOrElseThrow(examId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));
		AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		//시험 아이디와 답안지 시험 아이디 불일치
		if (!examId.equals(answerSheet.getExam().getId())) {
			throw new ApiException(ErrorStatus.ACCESS_DENIED);
		}

		//관리자만 삭제 가능
		if (!user.getRole().equals(Role.ADMIN)) {
			throw new ApiException(ErrorStatus.ACCESS_DENIED);
		}

		// userAnswerRepository.deleteAllByAnswerSheet(answerSheet);
		answerSheetRepository.delete(answerSheet);
	}

	//답안 최종 제출
	@Transactional
	public AnswerSheetResponseDto.Submit submitAnswerSheet(Long examId, Long answerSheetId,
			AnswerSheetRequestDto requestDto, Long userId) {
		Exam exam = examRepository.findByIdOrElseThrow(examId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		if (answerSheet.getStatus() == AnswerSheetStatus.SUBMITTED
				|| answerSheet.getStatus() == AnswerSheetStatus.GRADED) {
			throw new ApiException(ErrorStatus.ANSWER_SUBMITTED);
		}

		//본인이나 관리자가 아니면 에러
		if (!answerSheet.getExam().getId().equals(exam.getId()) ||
				!(answerSheet.getUser().getId().equals(user.getId()) || user.getRole()
						.equals(Role.ADMIN))) {
			throw new ApiException(ErrorStatus.ACCESS_DENIED);
		}

		saveUserAnswers(requestDto, answerSheet);

		answerSheet.updateStatus(AnswerSheetStatus.SUBMITTED);

		List<UserAnswer> userAnswers = userAnswerRepository.findAllByAnswerSheet(answerSheet);

		List<UserAnswerResponseDto> answerDtos = sortUserAnswers(userAnswers);

		return new AnswerSheetResponseDto.Submit(
				exam.getId(),
				user.getId(),
				answerSheet.getStatus(),
				answerDtos
		);
	}

	//시험 응시자 조회
	@Transactional(readOnly = true)
	public List<AnswerSheetResponseDto.Applicant> getExamApplicants(Long examId, Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

		if (!user.getRole().equals(Role.ADMIN)) {
			throw new ApiException(ErrorStatus.ACCESS_DENIED);
		}

		List<AnswerSheet> answerSheets = answerSheetRepository.findByExamId(examId);
		return answerSheets.stream()
				.map(answerSheet -> new AnswerSheetResponseDto.Applicant(
						answerSheet.getUser().getUsername(),
						answerSheet.getUser().getEmail(),
						answerSheet.getStatus()
				))
				.toList();
	}

	//답안지 채점
	@Transactional
	public void gradeAnswerSheet(Long answerSheetId) {
		AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		List<UserAnswer> userAnswers = userAnswerRepository.findAllByAnswerSheet(answerSheet);

		int score = 0;
		for (UserAnswer userAnswer : userAnswers) {
			ExamAnswer examAnswer = examAnswerRepository.findByExamAndQuestionNumber(
							answerSheet.getExam(),
							userAnswer.getQuestionNumber())
					.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_ANSWER_NOT_FOUND));

			String correctAnswer = examAnswer.getCorrectAnswer();

			if (correctAnswer.equals(userAnswer.getAnswerText())) {
				score += examAnswer.getQuestionScore();
			}
		}
		answerSheet.grade(score);
		answerSheetRepository.save(answerSheet);

		// sms 저장 및 전송 호출
		smsService.createSms(
				answerSheet.getUser().getId(),
				answerSheet.getExam().getId(),
				score
		);
	}

	//답안지 상태 변경
	@Transactional
	public void changeAnswerSheetStatus(Long answerSheetId) {
		AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
				.orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

		answerSheet.updateStatus(AnswerSheetStatus.SUBMITTED);
	}

	//답안 저장 로직
	public void saveUserAnswers(AnswerSheetRequestDto requestDto, AnswerSheet answerSheet) {
		for (UserAnswerRequestDto dto : requestDto.getAnswers()) {
			int questionNumber = dto.getQuestionNumber();
			String answerText = dto.getAnswerText();

			Optional<UserAnswer> userAnswer = userAnswerRepository.findByAnswerSheetAndQuestionNumber(
					answerSheet,
					questionNumber);

			if (userAnswer.isPresent()) {
				userAnswer.get().updateAnswer(answerText);
			} else {
				UserAnswer newAnswer = new UserAnswer(answerSheet, questionNumber, answerText);
				userAnswerRepository.save(newAnswer);
			}
		}
	}

	//답변 정렬 로직
	private List<UserAnswerResponseDto> sortUserAnswers(List<UserAnswer> userAnswers) {
		return userAnswers.stream()
				.map(UserAnswerResponseDto::toUserAnswerResponseDto)
				.sorted(Comparator.comparing(UserAnswerResponseDto::getQuestionNumber))
				.toList();
	}
}