package com.example.onlineexamplatform.domain.userAnswer.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.answerSheet.repository.AnswerSheetRepository;
import com.example.onlineexamplatform.domain.examAnswer.repository.ExamAnswerRepository;
import com.example.onlineexamplatform.domain.userAnswer.dto.SaveAnswerDto;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final AnswerSheetRepository answerSheetRepository;

    private final ExamAnswerRepository examAnswerRepository;

    @Transactional
    public void saveAnswer(Long answerSheetId, List<SaveAnswerDto> answers) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new ApiException(ErrorStatus.ANSWER_SHEET_NOT_FOUND));

        // 사용자 제출 답안 questionNumber 중복 체크
        if(answers.size() != answers.stream().map(SaveAnswerDto::getQuestionNumber).distinct().count()) {
            throw new ApiException(ErrorStatus.DUPLICATE_QUESTION_NUMBER); }

        // 시험 문제보다 제출한 문제가 더 많을경우 예외 발생
        int answerCount = examAnswerRepository.countByExamId(answerSheet.getExam().getId());

        if(answers.size() > answerCount) {
            throw new ApiException(ErrorStatus.EXCEED_USER_ANSWER);
        }

        // DB에 저장된 값 한번에 불러오기 -> SELECT 1회
        Map<Integer, UserAnswer> userAnswerMap = userAnswerRepository.findAllByAnswerSheetId(answerSheetId)
                .stream()
                .collect(Collectors.toMap(UserAnswer::getQuestionNumber, Function.identity()));

        for(SaveAnswerDto dto : answers) {
            String saveAnswer = dto.getAnswerText();
            Integer saveQuestionNumber = dto.getQuestionNumber();

            if (userAnswerMap.containsKey(saveQuestionNumber)) {
                userAnswerMap.get(saveQuestionNumber).updateAnswer(saveAnswer);
            } else {
                UserAnswer userAnswer = new UserAnswer(answerSheet, saveQuestionNumber, saveAnswer);
                userAnswerRepository.save(userAnswer);
            }
        }
    }
}
