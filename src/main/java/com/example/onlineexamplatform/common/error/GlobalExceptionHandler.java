package com.example.onlineexamplatform.common.error;

import com.example.onlineexamplatform.common.code.ErrorStatus;
import com.example.onlineexamplatform.common.dto.ErrorReasonDto;
import com.example.onlineexamplatform.common.response.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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

	// enum 파싱 실패 등 커스텀 예외 처리
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {

		log.error("Invalid request body or enum format: {}", ex.getMessage(), ex);

		Throwable cause = ex.getCause();

		if (cause instanceof InvalidFormatException formatEx) {
			if (!formatEx.getPath().isEmpty()) {
				String fieldName = formatEx.getPath().get(0).getFieldName();
				// 필드명이 categoryType이면 전용 에러 반환
				if ("categoryType".equals(fieldName)) {
					return ResponseEntity
							.status(ErrorStatus.INVALID_CATEGORY_TYPE.getHttpStatus())
							.body(ApiResponse.onFailure(ErrorStatus.INVALID_CATEGORY_TYPE));
				}
			}
		}
		// 그 외 모든 JSON 파싱 오류는 일반 BAD_REQUEST 처리
		return ResponseEntity
				.status(ErrorStatus.BAD_REQUEST.getHttpStatus())
				.body(ApiResponse.onFailure(ErrorStatus.BAD_REQUEST));
	}

}