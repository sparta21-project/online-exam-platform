package com.example.onlineexamplatform.domain.examAnswer.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.examAnswer.dto.ExamAnswerResponseDto;
import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamAnswerService {

    private final ExamAnswerRepository examAnswerRepository;

    private final ExamRepository examRepository;

    public void saveExamAnswer(Long examId, int questionNumber, int questionScore, String correctAnswer) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Optional<ExamAnswer> existExamAnswer = examAnswerRepository.findByExamIdAndQuestionNumber(exam.getId(), questionNumber);

        if(existExamAnswer.isPresent()) {
            throw new ApiException(ErrorStatus.DUPLICATE_EXAM_ANSWER);
        }

        ExamAnswer examAnswer = new ExamAnswer(exam, questionNumber, questionScore, correctAnswer);

        examAnswerRepository.save(examAnswer);
    }

    @Transactional
    public void updateExamAnswer(Long examAnswerId, Integer questionNumber, Integer questionScore, String correctAnswer) {
        ExamAnswer examAnswer = examAnswerRepository.findById(examAnswerId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        if(questionNumber != null) {
            examAnswer.updateQuestionNumber(questionNumber);
        }

        if(questionScore != null) {
            examAnswer.updateQuestionScore(questionScore);
        }

        if(correctAnswer != null) {
            examAnswer.updateCorrectAnswer(correctAnswer);
        }
    }

    public ExamAnswerResponseDto getExamAnswer(Long examAnswerId) {
        ExamAnswer examAnswer = examAnswerRepository.findById(examAnswerId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        return new ExamAnswerResponseDto(examAnswer.getExam().getId(),
                examAnswer.getQuestionNumber(),
                examAnswer.getQuestionScore(),
                examAnswer.getCorrectAnswer());
    }

    public Page<ExamAnswerResponseDto> getAllExamAnswer(Long examId, Pageable pageable) {
        return examAnswerRepository.findAllByExamId(examId, pageable)
                .map(ExamAnswerResponseDto::new);
    }
}
