package com.example.onlineexamplatform.domain.examAnswer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveExamAnswerRequestDto {

    @NotBlank
    @Valid
    private List<SaveExamAnswerDto> examAnswers;
}
