package com.example.onlineexamplatform.domain.userAnswer.service;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.domain.answerSheet.entity.AnswerSheet;
import com.example.onlineexamplatform.domain.userAnswer.entity.UserAnswer;
import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    private final AnswerSheetRepository answerSheetRepository;

    @Transactional
    public void saveAnswer(Long answerSheetId, int questionNumber, String answerText) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new ApiException(ErrorStatus.USER_NOT_FOUND));

        Optional<UserAnswer> existUserAnswer = userAnswerRepository.findByAnswerSheetIdAndQuestionNumber(answerSheet.getId(), questionNumber);

        if (existUserAnswer.isPresent()) {
            existUserAnswer.get().updateAnswer(answerText);
        } else {
            UserAnswer userAnswer = new UserAnswer(answerSheet, questionNumber, answerText);
            userAnswerRepository.save(userAnswer);
        }
    }
}
