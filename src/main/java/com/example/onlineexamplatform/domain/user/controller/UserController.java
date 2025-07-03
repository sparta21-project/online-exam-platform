package com.example.onlineexamplatform.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyRequest;
import com.example.onlineexamplatform.domain.user.dto.UserProfileModifyResponse;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "03 User", description = "사용자 프로필 조회,수정,탈퇴 API")
public class UserController {

	private final UserService userService;

	// 프로필 조회
	@CheckAuth(Role.USER)
	@GetMapping
	@Operation(summary = "내 프로필 조회", description = "로그인된 사용자의 프로필 정보를 반환합니다.")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
		@UserSession SessionUser session
	) {
		Long userId = session.getUserId();

		UserProfileResponse dto = userService.getProfile(userId);
		return ApiResponse.onSuccess(SuccessStatus.GETMYINFO_SUCCESS, dto);
	}

	// 프로필 수정
	@CheckAuth(Role.USER)
	@Operation(summary = "내 프로필 수정", description = "로그인된 사용자의 프로필 정보를 수정합니다.")
	@Parameter(description = "프로필 수정 요청 정보")
	@PutMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileModifyResponse>> modifyProfile(
		@RequestBody @Valid UserProfileModifyRequest modifyrequest,
		@UserSession SessionUser session
	) {
		Long userId = session.getUserId();

		UserProfileModifyResponse updated = userService.modifyProfile(userId, modifyrequest);
		return ApiResponse.onSuccess(SuccessStatus.UPDATE_PROFILE_SUCCESS, updated);
	}

	// 회원탈퇴
	@CheckAuth(Role.USER)
	@Operation(summary = "내 계정 탈퇴", description = "로그인된 사용자의 계정을 삭제하고 세션을 무효화합니다.")
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAccount(@UserSession SessionUser session) {
		Long userId = session.getUserId();

		userService.delete(userId);
		return ApiResponse.onSuccess(SuccessStatus.DELETE_SUCCESS);
	}
}
