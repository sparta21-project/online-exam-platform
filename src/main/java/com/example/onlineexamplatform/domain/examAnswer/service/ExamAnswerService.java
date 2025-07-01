package com.example.onlineexamplatform.domain.examAnswer.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.exam.repository.ExamRepository;
import com.example.onlineexamplatform.domain.examAnswer.dto.ExamAnswerResponseDto;
import com.example.onlineexamplatform.domain.examAnswer.dto.SaveExamAnswerDto;
import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamAnswerService {

	private final ExamAnswerRepository examAnswerRepository;

	private final ExamRepository examRepository;

	@Transactional
	public void saveExamAnswer(Long examId, List<SaveExamAnswerDto> examAnswers, Long userId) {
		Exam exam = examRepository.findById(examId)
			.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_NOT_FOUND));

		// 시험 작성자 확인
		if (!exam.getUser().getId().equals(userId)) {
			throw new ApiException(ErrorStatus.FORBIDDEN);
		}

		// 사용자 제출 답안 questionNumber 중복 체크
		if (examAnswers.size() != examAnswers.stream().map(SaveExamAnswerDto::getQuestionNumber).distinct().count()) {
			throw new ApiException(ErrorStatus.DUPLICATE_QUESTION_NUMBER);
		}

		Map<Integer, ExamAnswer> examAnswerMap = examAnswerRepository.findAllByExamId(examId)
			.stream()
			.collect(Collectors.toMap(ExamAnswer::getQuestionNumber, Function.identity()));

		for (SaveExamAnswerDto dto : examAnswers) {
			Integer saveExamQuestionNumber = dto.getQuestionNumber();
			String saveExamCorrectAnswer = dto.getCorrectAnswer();
			Integer saveExamQuestionScore = dto.getQuestionScore();

			if (examAnswerMap.containsKey(saveExamQuestionNumber)) {
				examAnswerMap.get(saveExamQuestionNumber)
					.updateExamAnswer(saveExamQuestionScore, saveExamCorrectAnswer);
			} else {
				ExamAnswer examAnswer = new ExamAnswer(exam, saveExamQuestionNumber, saveExamQuestionScore,
					saveExamCorrectAnswer);
				examAnswerRepository.save(examAnswer);
			}
		}
	}

	@Transactional(readOnly = true)
	public ExamAnswerResponseDto getExamAnswer(Long examAnswerId) {
		ExamAnswer examAnswer = examAnswerRepository.findById(examAnswerId)
			.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_ANSWER_NOT_FOUND));

		return new ExamAnswerResponseDto(examAnswer.getExam().getId(),
			examAnswer.getQuestionNumber(),
			examAnswer.getQuestionScore(),
			examAnswer.getCorrectAnswer());
	}

	@Transactional(readOnly = true)
	public Page<ExamAnswerResponseDto> getAllExamAnswer(Long examId, Pageable pageable) {
		return examAnswerRepository.findAllByExamId(examId, pageable)
			.map(ExamAnswerResponseDto::new);
	}

	@Transactional
	public void deleteExamAnswer(Long examAnswerId, Long userId) {
		ExamAnswer examAnswer = examAnswerRepository.findById(examAnswerId)
			.orElseThrow(() -> new ApiException(ErrorStatus.EXAM_ANSWER_NOT_FOUND));

		// 해당 시험의 작성자가 본인인지 확인
		if (!examAnswer.getExam().getUser().getId().equals(userId)) {
			throw new ApiException(ErrorStatus.FORBIDDEN);
		}

		examAnswerRepository.deleteById(examAnswerId);
	}
}
