package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.user.dto.UserLoginRequest;
import com.example.onlineexamplatform.domain.user.dto.UserLoginResponse;
import com.example.onlineexamplatform.domain.user.dto.UserPasswordRequest;
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

	@PutMapping("/password")
	public ResponseEntity<ApiResponse<Void>> changePassword(
		@RequestBody @Valid UserPasswordRequest request,
		HttpSession session
	) {
		Long userId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.INVALID_PASSWORD);
		}

		userService.changePassword(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.UPDATE_PASSWORD);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
		Long userId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		session.invalidate();
		return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
	}

}
