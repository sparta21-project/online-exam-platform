package com.example.onlineexamplatform.domain.sms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineexamplatform.common.code.SuccessStatus;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.example.onlineexamplatform.config.session.CheckAuth;
import com.example.onlineexamplatform.config.session.SessionUser;
import com.example.onlineexamplatform.config.session.UserSession;
import com.example.onlineexamplatform.domain.sms.dto.SmsResponse;
import com.example.onlineexamplatform.domain.sms.service.SmsService;
import com.example.onlineexamplatform.domain.user.entity.Role;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {

	private final SmsService smsService;

	// sms 조회
	@CheckAuth(Role.USER)
	@GetMapping
	public ResponseEntity<ApiResponse<List<SmsResponse>>> getSmsList(@UserSession SessionUser sessionUser) {
		List<SmsResponse> smsList = smsService.getSmsList(sessionUser.getUserId());
		return ApiResponse.onSuccess(SuccessStatus.GET_SMS_LIST_SUCCESS, smsList);
	}
}