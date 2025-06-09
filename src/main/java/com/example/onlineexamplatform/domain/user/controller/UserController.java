package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.user.dto.ProfileModifyRequestDto;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	public static final String SESSION_USER_KEY = "LOGIN_USER_ID";

	// 프로필 조회
	@GetMapping
	public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
		HttpSession session
	) {
		Long userId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		UserProfileResponse dto = userService.getProfile(userId);
		return ApiResponse.onSuccess(SuccessStatus.GETMYINFO_SUCCESS, dto);
	}

	// 프로필 수정
	@PutMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> modifyProfile(
		@RequestBody @Valid ProfileModifyRequestDto request,
		HttpSession session
	) {
		Long userId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		UserProfileResponse updated = userService.modifyProfile(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.UPDATE_PROFILE_SUCCESS, updated);
	}

	// 회원탈퇴
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAccount(HttpSession session) {
		Long sessionUserId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (sessionUserId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		userService.delete(sessionUserId);
		session.invalidate();
		return ApiResponse.onSuccess(SuccessStatus.DELETE_SUCCESS);
	}
}
