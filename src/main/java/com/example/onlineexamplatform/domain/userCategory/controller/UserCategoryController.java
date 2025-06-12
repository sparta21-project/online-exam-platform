package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.user.entity.Role;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-category")
@RequiredArgsConstructor
@Tag(name = "User UserCategory", description = "사용자 전용 응시 권한 조회 API")
public class UserCategoryController {

	private final UserCategoryService userCategoryService;

	@CheckAuth(Role.USER)
	@Operation(summary = "내 응시 권한 목록 조회", description = "로그인한 사용자의 응시 권한 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUser(
			HttpServletRequest request) {
		UserSession session = (UserSession) request.getAttribute("userSession");
		Long userId = session.getUserId();
		List<UserCategoryResponse> responseList = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, responseList);
	}
}

