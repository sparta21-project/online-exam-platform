package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.user.dto.UserLoginRequest;
import com.example.onlineexamplatform.domain.user.dto.UserLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.UserSignupRequest;
import com.example.onlineexamplatform.domain.user.dto.UserSignupResponse;
import com.example.onlineexamplatform.domain.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	public static final String SESSION_USER_KEY = "LOGIN_USER_ID";

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<UserSignupResponse>> signup(
		@RequestBody @Valid UserSignupRequest request
	) {
		UserSignupResponse dto = userService.signup(request);
		return ApiResponse.onSuccess(SuccessStatus.SIGNUP_SUCCESS, dto);
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserLoginResponse>> login(
		@RequestBody @Valid UserLoginRequest request,
		HttpSession session
	) {
		UserLoginResponse dto = userService.login(request);
		session.setAttribute(SESSION_USER_KEY, dto.getId());
		return ApiResponse.onSuccess(SuccessStatus.LOGIN_SUCCESS, dto);
	}
}
