package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.user.dto.UserSignupRequest;
import com.example.onlineexamplatform.domain.user.dto.UserSignupResponse;
import com.example.onlineexamplatform.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<UserSignupResponse>> signup(
		@RequestBody @Valid UserSignupRequest request
	) {
		UserSignupResponse dto = userService.signup(request);
		return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, dto);
	}
}
