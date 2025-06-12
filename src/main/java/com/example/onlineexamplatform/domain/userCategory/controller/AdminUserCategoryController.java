package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.dto.UserProfileResponse;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryRequest;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 전용 응시 권한 관리 컨트롤러 - 관리자가 특정 사용자에게 응시 권한을 부여, 조회, 삭제할 수 있음 - 모든 요청은 세션에서 ADMIN 권한 여부를 확인
 */
@RestController
@RequestMapping("/api/admin/user-category")
@RequiredArgsConstructor
@Tag(name = "Admin UserCategory", description = "관리자 전용 응시 권한 관리 API")
public class AdminUserCategoryController {

	private final UserCategoryService userCategoryService;

	private void checkAdmin(HttpServletRequest request) {
		UserSession session = (UserSession) request.getAttribute("userSession");
		if (session == null || session.getRole() != Role.ADMIN) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
	}

	@Operation(summary = "응시 권한 부여", description = "관리자가 특정 사용자에게 응시 권한을 부여합니다.")
	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserCategoryResponse>> createByAdmin(
			@Parameter(description = "응시 권한을 부여할 사용자 ID") @PathVariable Long userId,
			@Parameter(description = "부여할 카테고리 정보") @RequestBody @Valid UserCategoryRequest request,
			HttpServletRequest requestObj
	) {
		checkAdmin(requestObj);
		UserCategoryResponse response = userCategoryService.create(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_CREATE_SUCCESS, response);
	}

	@Operation(summary = "사용자별 응시 권한 조회", description = "관리자가 특정 사용자의 응시 권한 목록을 조회합니다.")
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUserId(
			@Parameter(description = "사용자 ID") @PathVariable Long userId,
			HttpServletRequest request
	) {
		checkAdmin(request);
		List<UserCategoryResponse> response = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, response);
	}

	@Operation(summary = "응시 권한 삭제", description = "관리자가 특정 사용자의 응시 권한을 삭제합니다.")
	@DeleteMapping("/{userCategoryId}")
	public ResponseEntity<ApiResponse<Void>> deleteByAdmin(
			@Parameter(description = "삭제할 응시 권한 ID") @PathVariable Long userCategoryId,
			HttpServletRequest request
	) {
		checkAdmin(request);
		userCategoryService.delete(userCategoryId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_DELETE_SUCCESS);
	}

	@Operation(summary = "응시권한별 사용자 목록 조회", description = "관리자가 특정 응시 권한을 보유한 사용자 목록을 조회합니다.")
	@GetMapping("/category/{categoryType}")
	public ResponseEntity<ApiResponse<List<UserProfileResponse>>> getUsersByCategory(
			@Parameter(description = "카테고리 타입 (예: MATH, HISTORY)") @PathVariable String categoryType,
			HttpServletRequest request
	) {
		checkAdmin(request);
		List<UserProfileResponse> response = userCategoryService.getUsersByCategory(categoryType);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_USERS_BY_CATEGORY_SUCCESS, response);
	}
}
