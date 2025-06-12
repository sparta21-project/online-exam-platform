package com.example.onlineexamplatform.domain.userCategory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryRequest;
import com.example.onlineexamplatform.domain.userCategory.dto.UserCategoryResponse;
import com.example.onlineexamplatform.domain.userCategory.service.UserCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-category")
@RequiredArgsConstructor

public class UserCategoryController {

	private final UserCategoryService userCategoryService;

	@PostMapping
	public ResponseEntity<ApiResponse<UserCategoryResponse>> create(
		@RequestBody @Valid UserCategoryRequest request,
		HttpServletRequest servletRequest
	) {
		UserSession session = (UserSession)servletRequest.getAttribute("userSession");
		Long userId = session.getUserid();
		UserCategoryResponse response = userCategoryService.create(userId, request);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_CREATE_SUCCESS, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserCategoryResponse>>> getByUser(HttpServletRequest servletRequest) {
		UserSession session = (UserSession)servletRequest.getAttribute("userSession");
		Long userId = session.getUserid();
		List<UserCategoryResponse> responseList = userCategoryService.getByUser(userId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_GET_SUCCESS, responseList);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long categoryId,
		HttpServletRequest servletRequest) {
		UserSession session = (UserSession)servletRequest.getAttribute("userSession");
		Long userId = session.getUserid();
		userCategoryService.delete(userId, categoryId);
		return ApiResponse.onSuccess(SuccessStatus.USERCATEGORY_DELETE_SUCCESS);
	}
}
