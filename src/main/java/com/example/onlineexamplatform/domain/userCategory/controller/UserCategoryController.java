package com.example.onlineexamplatform.domain.userCategory.controller;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.domain.user.controller.UserController;
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

@RestController
@RequestMapping("/api/user-category")
@RequiredArgsConstructor


public class UserCategoryController {

	private final UserCategoryService userCategoryService;

	@PostMapping
	public ResponseEntity<ApiResponse<UserCategoryResponse>> create(
			@RequestBody @Valid UserCategoryRequest request,
			HttpSession session
	) {
		Long userId = (Long) session.getAttribute(UserController.SESSION_USER_KEY);
		UserCategoryResponse response = userCategoryService.create(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_CREATE_SUCCESS, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUser(HttpSession session) {
		Long userId = (Long) session.getAttribute(UserController.SESSION_USER_KEY);
		List<UserCategoryResponse> responseList = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, responseList);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long categoryId,
			HttpSession session) {
		Long userId = (Long) session.getAttribute(UserController.SESSION_USER_KEY);
		userCategoryService.delete(userId, categoryId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_DELETE_SUCCESS);
	}
}
