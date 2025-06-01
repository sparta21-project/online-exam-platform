package com.example.onlineexamplatform.domain.userAnswer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveAnswerRequestDto {
    @NotBlank
    @Valid
    private List<SaveAnswerDto> answers;

}
