package com.example.onlineexamplatform.domain.user.controller;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin User", description = "관리자 전용 사용자 관리 API")
public class AdminUserController {

	private final UserService userService;

	private void checkAdmin(HttpServletRequest request) {
		UserSession session = (UserSession) request.getAttribute("userSession");
		if (session == null || session.getRole() != Role.ADMIN) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
	}

	@Operation(summary = "전체 사용자 목록 조회", description = "관리자가 모든 사용자 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getAllUsers(
			@Parameter(description = "사용자 이름 검색 필터", required = false) @RequestParam(required = false) String name,
			@Parameter(description = "사용자 이메일 검색 필터", required = false) @RequestParam(required = false) String email,
			HttpServletRequest request
	) {
		checkAdmin(request);
		List<UserProfileResponse> users = userService.getUsersByFilter(name, email);
		return ApiResponse.onSuccess(SuccessStatus.USER_GET_ALL_SUCCESS, users);
	}
}
