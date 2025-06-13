package com.example.onlineexamplatform.domain.examAnswer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveExamAnswerRequestDto {

    @NotNull
    @Valid
    private List<SaveExamAnswerDto> examAnswers;
}
