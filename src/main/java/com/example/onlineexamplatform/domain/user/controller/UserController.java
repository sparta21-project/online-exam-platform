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
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyRequest;
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyResponse;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 프로필 조회,수정,탈퇴 API")
public class UserController {

	private final UserService userService;
	public static final String SESSION_USER_KEY = "LOGIN_USER_ID";

	// 프로필 조회
	@GetMapping
	@Operation(summary = "내 프로필 조회", description = "로그인된 사용자의 프로필 정보를 반환합니다.")
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
	@Operation(summary = "내 프로필 수정", description = "로그인된 사용자의 프로필 정보를 수정합니다.")
	@Parameter(description = "프로필 수정 요청 정보")
	@PutMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileModifyResponse>> modifyProfile(
		@RequestBody @Valid UserProfileModifyRequest request,
		HttpSession session
	) {
		Long userId = (Long)session.getAttribute(SESSION_USER_KEY);
		if (userId == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		UserProfileModifyResponse updated = userService.modifyProfile(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.UPDATE_PROFILE_SUCCESS, updated);
	}

	// 회원탈퇴
	@Operation(summary = "내 계정 탈퇴", description = "로그인된 사용자의 계정을 삭제하고 세션을 무효화합니다.")
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
