package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.error.ApiException;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-category")
@RequiredArgsConstructor
@Tag(name = "User UserCategory", description = "사용자 전용 응시 권한 조회 API")
public class UserCategoryController {

	private final UserCategoryService userCategoryService;

	@Operation(summary = "내 응시 권한 목록 조회", description = "로그인한 사용자의 응시 권한 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUser(
			HttpServletRequest request) {
		UserSession session = (UserSession) request.getAttribute("userSession");
		if (session == null) {
			throw new ApiException(ErrorStatus.UNAUTHORIZED);
		}

		Long userId = session.getUserId();
		List<UserCategoryResponse> responseList = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, responseList);
	}
}
