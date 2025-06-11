package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.common.util.SessionUserUtil;
import com.example.onlineexamplatform.domain.user.dto.UserSummaryResponse;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryRequest;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;
import jakarta.servlet.http.HttpSession;
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
 * 관리자 전용 응시 권한 관리 컨트롤러
 * - 관리자가 특정 사용자에게 응시 권한을 부여, 조회, 삭제할 수 있음
 * - 모든 요청은 세션에서 ADMIN 권한 여부를 확인
 */
@RestController
@RequestMapping("/api/admin/user-category")
@RequiredArgsConstructor
public class AdminUserCategoryController {

	private final UserCategoryService userCategoryService;
	private final SessionUserUtil sessionUserUtil;


	@PostMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserCategoryResponse>> createByAdmin(
			@PathVariable Long userId,
			@RequestBody @Valid UserCategoryRequest request,
			HttpSession session
	) {
		if (!sessionUserUtil.isAdmin(session)) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
		UserCategoryResponse response = userCategoryService.create(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_CREATE_SUCCESS, response);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUserId(
			@PathVariable Long userId,
			HttpSession session
	) {
		if (!sessionUserUtil.isAdmin(session)) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
		List<UserCategoryResponse> response = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, response);
	}

	@DeleteMapping("/{userCategoryId}")
	public ResponseEntity<ApiResponse<Void>> deleteByAdmin(
			@PathVariable Long userCategoryId,
			HttpSession session
	) {
		if (!sessionUserUtil.isAdmin(session)) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
		userCategoryService.delete(userCategoryId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_DELETE_SUCCESS);
	}

	@GetMapping("/category/{categoryType}")
	public ResponseEntity<ApiResponse<List<UserSummaryResponse>>> getUsersByCategory(
			@PathVariable String categoryType,
			HttpSession session
	) {
		if (!sessionUserUtil.isAdmin(session)) {
			throw new ApiException(ErrorStatus.FORBIDDEN_ADMIN_ONLY);
		}
		List<UserSummaryResponse> response = userCategoryService.getUsersByCategory(categoryType);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_USERS_BY_CATEGORY_SUCCESS, response);
	}
}
