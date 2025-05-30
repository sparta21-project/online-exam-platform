package com.example.onlineexamplatform.domain.userAnswer.service;

import com.example.onlineexamplatform.domain.userAnswer.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;

    public static void saveAnswer(Long answerSheetId, int questionNumber, String answerText) {



    }
}
