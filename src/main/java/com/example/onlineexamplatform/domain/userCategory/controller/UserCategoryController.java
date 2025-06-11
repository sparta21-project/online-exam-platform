package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.common.util.SessionUserUtil;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 일반 사용자 전용 컨트롤러
 * - 본인이 가지고 있는 응시 권한 목록을 조회 가능
 */
@RestController
@RequestMapping("/api/user-category")
@RequiredArgsConstructor
public class UserCategoryController {

	private final UserCategoryService userCategoryService;
	private final SessionUserUtil sessionUserUtil;

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUser(HttpSession session) {
		Long userId = sessionUserUtil.getCurrentUserId(session);
		List<UserCategoryResponse> responseList = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, responseList);
	}
}
