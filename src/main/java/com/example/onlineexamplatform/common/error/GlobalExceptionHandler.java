package com.example.onlineexamplatform.common.error;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.dto.ErrorReasonDto;
import com.example.onlineexamplatform.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// 커스텀 예외 처리
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<ErrorReasonDto>> handleCustomException(ApiException e) {
		log.error("CustomException: {}", e.getMessage(), e);
		return ApiResponse.onFailure(e.getErrorCode());
	}

	// enum 값 등 잘못된 요청 바디 처리
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<? extends ApiResponse<?>> handleInvalidEnum(HttpMessageNotReadableException e) {
		log.error("Invalid request format: {}", e.getMessage(), e);
		return ApiResponse.onFailure(ErrorStatus.INVALID_ENUM_VALUE);
	}

}