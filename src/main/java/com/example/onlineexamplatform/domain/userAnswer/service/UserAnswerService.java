package com.example.onlineexamplatform.domain.userAnswer.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;
import com.example.onlineexamplatform.domain.userAnswer.dto.SaveAnswerDto;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final AnswerSheetRepository answerSheetRepository;

    private final ExamAnswerRepository examAnswerRepository;

    private final MeterRegistry meterRegistry;

    @Transactional
    public void saveAnswer(Long answerSheetId, List<SaveAnswerDto> answers) {
        Sample sample = Timer.start(meterRegistry);

        try {
            AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                    .orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

            if (answers.size() != answers.stream().map(SaveAnswerDto::getQuestionNumber).distinct().count()) {
                throw new ApiException(ErrorStatus.DUPLICATE_QUESTION_NUMBER);
            }

            int answerCount = examAnswerRepository.countByExamId(answerSheet.getExam().getId());
            if (answers.size() > answerCount) {
                throw new ApiException(ErrorStatus.EXCEED_USER_ANSWER);
            }

            Map<Integer, UserAnswer> userAnswerMap = userAnswerRepository.findAllByAnswerSheetId(answerSheetId)
                    .stream()
                    .collect(Collectors.toMap(UserAnswer::getQuestionNumber, Function.identity()));

            for (SaveAnswerDto dto : answers) {
                String answer = dto.getAnswerText();
                Integer questionNumber = dto.getQuestionNumber();

                if (userAnswerMap.containsKey(questionNumber)) {
                    userAnswerMap.get(questionNumber).updateAnswer(answer);
                } else {
                    userAnswerRepository.save(new UserAnswer(answerSheet, questionNumber, answer));
                }
            }
        } finally {
            sample.stop(Timer.builder("custom.save.answer.timer")
                    .description("SaveAnswer API (Map version)")
                    .tag("version", "map")
                    .register(meterRegistry));
        }
    }
}
