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

    /*
       50개의 항목 저장
       1. save, update -> UPSERT
       2. 50번의 API 호출 -> 1번의 API 호출
       3. 50번의 SELECT -> 1번 호출 List -> Map
       4. n번의 UPDATE
       5. 50-n번의 INSERT
    */

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

/*
    1. Optional 사용 vs 그냥 NULL 체크
    - 명시적으로 null 일수도 있다는것을 보여줌, NPE 발생 가능성 낮춤(get 전에 isPresent 등으로 확인), IDE 에서 NULL 관련 경고 확인 가능


    프론트에서 50개의 문항을 입력할수 있는 폼을 제공 -> 프론트를 믿지 마라..?
    List<SaveAnswerDto> 총 갯수 = examAnswerRepository.findAllByExamId

    questionNumber가 중복되서 입력되면??
    1. List<SaveAnswerDto> 에서 questionNumber를 꺼내서 중복이 있는지 비교
    2. toMap 에서 key 중복 오류 발생에서 처리

 */
