package com.example.onlineexamplatform.domain.userAnswer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.userAnswer.dto.SaveAnswerRequestDto;
import com.example.onlineexamplatform.domain.userAnswer.service.UserAnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "UserAnswer", description = "사용자(User)가 작성하는 답안 작성 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-answers")
public class UserAnswerController {

	private final UserAnswerService userAnswerService;

	@CheckAuth(Role.USER)
	@Operation(summary = "답안 입력(UPSERT)", description = "Dto로 입력받은 문제 번호와 답안을 저장합니다.")
	@Parameter(description = "시험정보, 사용자정보가 저장된 시험지(AnswerSheet)의 ID입니다.")
	@PostMapping("/{answerSheetId}")
	public ResponseEntity<ApiResponse<Void>> saveAnswer(@PathVariable Long answerSheetId,
		@RequestBody @Valid SaveAnswerRequestDto requestDto,
		@UserSession SessionUser sessionUser
	) {
		Long userId = sessionUser.getUserId();
		userAnswerService.saveAnswer(answerSheetId, requestDto.getAnswers(), userId);
		return ApiResponse.onSuccess(SuccessStatus.SAVE_USER_ANSWER_SUCCESS);
	}

}
