package com.example.onlineexamplatform.domain.userAnswer.controller;


import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.userAnswer.dto.SaveAnswerRequestDto;
import com.example.onlineexamplatform.domain.userAnswer.service.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userAnswers")
public class UserAnswerController {

    private final UserAnswerService userAnswerService;

    @PostMapping("/{answerSheetId}")
    public ResponseEntity<ApiResponse<Void>> saveAnswer(@PathVariable Long answerSheetId, @RequestBody SaveAnswerRequestDto requestDto) {
        userAnswerService.saveAnswer(answerSheetId, requestDto.getQuestionNumber(), requestDto.getAnswerText());
        return ApiResponse.onSuccess(SuccessStatus.SAVE_ANSWER_SUCCESS);
    }

}
