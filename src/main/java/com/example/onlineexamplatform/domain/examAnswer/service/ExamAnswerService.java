package com.example.onlineexamplatform.domain.examAnswer.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.exam.entity.Exam;
import com.example.onlineexamplatform.domain.examAnswer.dto.ExamAnswerResponseDto;
import com.example.onlineexamplatform.domain.examAnswer.dto.SaveExamAnswerDto;
import com.example.onlineexamplatform.domain.examAnswer.entity.ExamAnswer;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamAnswerService {

    private final ExamAnswerRepository examAnswerRepository;

    private final ExamRepository examRepository;

    @Transactional
    public void saveExamAnswer(Long examId, List<SaveExamAnswerDto> examAnswers) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Map<Integer, ExamAnswer> examAnswerMap = examAnswerRepository.findAllByExamId(examId)
                .stream()
                .collect(Collectors.toMap(ExamAnswer::getQuestionNumber, Function.identity()));

        for(SaveExamAnswerDto dto : examAnswers) {
            Integer saveExamQuestionNumber = dto.getQuestionNumber();
            String saveExamCorrectAnswer = dto.getCorrectAnswer();
            Integer saveExamQusetionScore = dto.getQuestionScore();

            if(examAnswerMap.containsKey(saveExamQuestionNumber)) {
                examAnswerMap.get(saveExamQuestionNumber).updateExamAnswer(saveExamQusetionScore, saveExamCorrectAnswer);
            } else {
                ExamAnswer examAnswer = new ExamAnswer(exam, saveExamQuestionNumber, saveExamQusetionScore, saveExamCorrectAnswer);
                examAnswerRepository.save(examAnswer);
            }
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

    public void deleteExamAnswer(Long examAnswerId) {
        examAnswerRepository.deleteById(examAnswerId);
    }
}
