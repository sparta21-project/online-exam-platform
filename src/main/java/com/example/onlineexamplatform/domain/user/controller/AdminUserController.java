package com.example.onlineexamplatform.domain.user.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "02 Admin User", description = "관리자 전용 사용자 관리 API")
public class AdminUserController {

	private final UserService userService;

	@CheckAuth(Role.ADMIN)
	@Operation(summary = " 02 - 전체 사용자 목록 조회", description = "관리자가 모든 사용자 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers(
		@Parameter(description = "사용자 이름 검색 필터") @RequestParam(required = false) String name,
		@Parameter(description = "사용자 이메일 검색 필터") @RequestParam(required = false) String email,
		@UserSession SessionUser sessionUser
	) {
		if (name != null)
			name = name.trim();
		if (email != null)
			email = email.trim();

		List<UserProfileResponse> users = userService.getUsersByFilter(name, email);
		SuccessStatus status = (name == null && email == null)
			? SuccessStatus.USER_GET_ALL_SUCCESS
			: SuccessStatus.USER_SEARCH_SUCCESS;

		return ApiResponse.onSuccess(status, users);
	}
}
