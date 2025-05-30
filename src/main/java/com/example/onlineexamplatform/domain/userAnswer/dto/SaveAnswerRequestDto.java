package com.example.onlineexamplatform.domain.userAnswer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveAnswerRequestDto {

    private int questionNumber;

    private String answerText;

}
